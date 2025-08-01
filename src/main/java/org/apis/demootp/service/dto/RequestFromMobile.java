package org.apis.demootp.service.dto;

public class RequestFromMobile {

    private String dateTime;
    private String otp;

    public void setOtp(String otp) {
        this.otp = otp;
    }
    public String getOtp() {
        return otp;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getDateTime() {
        return dateTime;
    }
}
