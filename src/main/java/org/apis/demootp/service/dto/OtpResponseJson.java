package org.apis.demootp.service.dto;

public class OtpResponseJson {
    private boolean success;
    private String message;

    public OtpResponseJson(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getOtp() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setOtp(int otp) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
