package org.apis.demootp.service.dto;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object
@Setter
@Getter
public class OtpMobileResponse {
    private String success;
    private String otpCode;

    OtpMobileResponse(String success, String otpCode) {
        this.success = success;
        this.otpCode = otpCode;
    }

}
