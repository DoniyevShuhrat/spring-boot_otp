package org.apis.demootp.web.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apis.demootp.domain.ReceivedSms;
import org.apis.demootp.service.OtpService;
import org.apis.demootp.service.dto.SmsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SmsApiController {
    Log logger = LogFactory.getLog(OtpResource.class);
    private final OtpService otpService;

    // Otpservice'ni automatic bog'laymiz (DI)
    public SmsApiController(OtpService otpService) {
        logger.info("SmsApiController created!");
        this.otpService = otpService;
    }

    @GetMapping("/sms/latest") // To'liq manzil: /api/sms/lastest
    public List<SmsDto> getLastestSms() {
        logger.info("getLastestSms");
        // 1. Servicedan barcha SMS ENtity'larini olamiz
        List<ReceivedSms> smsListFromDb = otpService.getAllSmsMessages();

        // 2. Entity listini DTO listiga o'tkazamiz
        // Bu bizga faqat kerakli maydonlarni (phoneNumber, smsMessage, createAt) qaytarish imkonini beradi
        return smsListFromDb.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yordamchi metod: ReceivedSms (Entity) dan SmsDto (DTO) yaratadi
    private SmsDto convertToDto(ReceivedSms sms) {
        SmsDto dto = new SmsDto();
        dto.setPhoneNumber(sms.getPhoneNumber());
        dto.setSmsMessage(sms.getSmsMessage());
        dto.setCreatedAt(sms.getCreatedAt());
        return dto;
    }
}
