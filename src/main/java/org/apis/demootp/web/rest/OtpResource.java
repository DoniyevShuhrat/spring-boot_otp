package org.apis.demootp.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OtpResource {
    @GetMapping("/otp")
    public String getOtp(){
        return "otp";
    }
}
