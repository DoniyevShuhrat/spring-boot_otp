package org.apis.demootp.service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data // Lombok getter va setter'larni avtomatik yaratadi
public class SmsDto {
    private String phoneNumber;
    private String smsMessage;
    private LocalDateTime createdAt;
}