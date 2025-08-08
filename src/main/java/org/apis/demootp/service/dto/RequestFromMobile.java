package org.apis.demootp.service.dto;

public class RequestFromMobile {

    private String dateTime;
    private String smsMessage;

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }
}
