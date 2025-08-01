package org.apis.demootp.service.dto;

public class OtpDateTimeResJson {
    private boolean success;
    private String message;
    private String dateTime;

    public OtpDateTimeResJson(boolean success, String message, String dateTime) {
        this.success = success;
        this.message = message;
        this.dateTime = dateTime;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
