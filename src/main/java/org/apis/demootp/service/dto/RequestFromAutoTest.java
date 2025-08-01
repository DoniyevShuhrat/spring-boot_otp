package org.apis.demootp.service.dto;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object
@Setter
@Getter
public class RequestFromAutoTest {
    private String phoneNumber;
    private String dateTime;

    RequestFromAutoTest(String phoneNumber, String dateTime) {
        this.phoneNumber = phoneNumber;
        this.dateTime = dateTime;
    }
}