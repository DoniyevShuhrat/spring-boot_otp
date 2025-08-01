package org.apis.demootp.service.dto;

public class ResponseToAutoTest {
    private String otp;
    private String status;

    public ResponseToAutoTest(String otp, String status) {
        this.otp = otp;
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
