package org.apis.demootp.service.dto;

// Data Transfer Object
public class OtpDateTimeDTO {
    private String otp;
    private String dateTime;

    public OtpDateTimeDTO() {
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }
}
