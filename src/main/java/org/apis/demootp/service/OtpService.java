package org.apis.demootp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apis.demootp.OtpUIController; // 1. Controller'ni import qilamiz
import org.apis.demootp.domain.ReceivedSms;
import org.apis.demootp.repository.ReceivedSmsRepository;
import org.apis.demootp.service.dto.RequestFromMobile;
import org.springframework.context.annotation.Lazy; // 2. @Lazy'ni import qilamiz
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apis.demootp.domain.SmsStatus;

@Service
public class OtpService {
    Log logger = LogFactory.getLog(this.getClass());
    private final Map<String, CompletableFuture<String>> otpWaiters = new ConcurrentHashMap<>();

    private final ReceivedSmsRepository receivedSmsRepository;

    // 3. OtpUIController uchun o'zgaruvchi e'lon qilamiz
    private final OtpUIController otpUIController;

    // 4. Contructor orqali OtpUIController'ni qabul qilamiz (Dependency Injection)
    public OtpService(@Lazy OtpUIController otpUIController, ReceivedSmsRepository receivedSmsRepository) {
        this.otpUIController = otpUIController;
        this.receivedSmsRepository = receivedSmsRepository;
    }

    public void submitOtp(RequestFromMobile requestFromMobile, String phoneNumber) {
        logger.info("submitOtp: otp=" + requestFromMobile.getSmsMessage() + ", dateTime=" + requestFromMobile.getDateTime());

        // Yangi kelgan SMS'ni database'ga qo'shamiz
        saveSmsToDatabase(requestFromMobile, phoneNumber);

        // 5. MUHIM QADAM: Kelgan OTP'ni UI'ga yuborish
        // Bu jonli yangilanish uchun UI'ga xabar yuboradi
        this.otpUIController.sendOtpUpdate(requestFromMobile);

        CompletableFuture<String> waiter = otpWaiters.get(phoneNumber); // remove qilamiz — bir marta kerak bo‘ladi
        if (waiter != null && !waiter.isDone()) {
            waiter.complete(requestFromMobile.getSmsMessage());
            otpWaiters.remove(phoneNumber); // Memory leak oldini olish
        } else {
            logger.warn("⚠️ OTP received but no one is waiting for phoneNumber: " + phoneNumber);
            logger.warn("Available waiting requests: " + otpWaiters.keySet());
        }
    }

    private void saveSmsToDatabase(RequestFromMobile requestFromMobile, String phoneNumber) {
        try {
            ReceivedSms newSms = new ReceivedSms();
            newSms.setPhoneNumber(phoneNumber);
            newSms.setSmsMessage(requestFromMobile.getSmsMessage());

            // DTO'dagi string sanani LocalDateTime'ga o'girish
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
            LocalDateTime sentDateTime = LocalDateTime.parse(requestFromMobile.getDateTime(), formatter);
            newSms.setSentAt(sentDateTime);

            // Status va yaroqlilik muddatini belgilash
            newSms.setStatus(SmsStatus.NEW); // String o'rniga Enum qiymatini ishlatamiz
            newSms.setExpiresAt(LocalDateTime.now().plusMinutes(2)); // Yaroqlilik muddati 2 daqiqa

            // createdAt va updatedAt bazaning o'zida DEFAULT NOW() orqali to'ldiriladi,
            // lekin trigger faqat UPDATE uchun ishlaydi, shuning uchun createdAt'ni kodda belgilaymiz
            newSms.setCreatedAt(LocalDateTime.now());
            newSms.setUpdatedAt(LocalDateTime.now());

            // OTP kodni ajratib olish logikasi
            // String otpCode = extractOtpCode(requestFromMobile.getSmsMessage());
            // newSms.setOtpCode(otpCode);
            receivedSmsRepository.save(newSms);
            logger.info("✅ New SMS for " + phoneNumber + " successfully saved to the database.");

        } catch (Exception e) {
            logger.error("❌ Failed to save SMS to database for phoneNumber: " + phoneNumber, e);
        }
    }

    public CompletableFuture<String> waiterForOtp(String phoneNumber) {
        logger.info("waiterForOtp: phoneNumber=" + phoneNumber);

        CompletableFuture<String> waiter = new CompletableFuture<>();
        otpWaiters.put(phoneNumber, waiter);

        logger.info("✅ Added to waiting queue. Current queue: " + otpWaiters.keySet());

        // Manual timeout handling - Spring async timeout bilan mos kelishi uchun
        CompletableFuture<String> timeoutFuture = waiter.orTimeout(60, TimeUnit.SECONDS);

        // Cleanup after timeout
        timeoutFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                logger.error("❌ Request timeout for phoneNumber: " + phoneNumber);
            } else {
                logger.info("✅ Request completed successfully for phoneNumber: " + phoneNumber);
            }
            otpWaiters.remove(phoneNumber);
        });

        return timeoutFuture;
    }

    public List<ReceivedSms> getAllSmsMessages() {
        // Repository orqali barcha yozuvlarni topib, qaytaramiz
        return receivedSmsRepository.findAll();
    }
}
