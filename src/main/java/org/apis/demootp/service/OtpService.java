package org.apis.demootp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apis.demootp.OtpUIController; // 1. Controller'ni import qilamiz
import org.apis.demootp.service.dto.RequestFromMobile;
import org.springframework.context.annotation.Lazy; // 2. @Lazy'ni import qilamiz
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private final Map<String, CompletableFuture<String>> otpWaiters = new ConcurrentHashMap<>();
    Log logger = LogFactory.getLog(this.getClass());

    // 3. OtpUIController uchun o'zgaruvchi e'lon qilamiz
    private final OtpUIController otpUIController;

    // 4. Contructor orqali OtpUIController'ni qabul qilamiz (Dependency Injection)
    public OtpService(@Lazy OtpUIController otpUIController) {
        this.otpUIController = otpUIController;
    }

    public void submitOtp(RequestFromMobile requestFromMobile, String phoneNumber){
        logger.info("submitOtp: otp=" + requestFromMobile.getSmsMessage() + ", dateTime=" + requestFromMobile.getDateTime());

        // 5. MUHIM QADAM: Kelgan OTP'ni UI'ga yuborish
        // Bu jonli yangilanish uchun UI'ga xabar yuboradi
        this.otpUIController.sendOtpUpdate(requestFromMobile);

        CompletableFuture<String> waiter = otpWaiters.get(phoneNumber); // remove qilamiz — bir marta kerak bo‘ladi
        if(waiter != null && !waiter.isDone()){
            waiter.complete(requestFromMobile.getSmsMessage());
            otpWaiters.remove(phoneNumber); // Memory leak oldini olish
        } else {
            logger.warn("⚠️ OTP received but no one is waiting for phoneNumber: " + phoneNumber);
            logger.warn("Available waiting requests: " + otpWaiters.keySet());
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
}
