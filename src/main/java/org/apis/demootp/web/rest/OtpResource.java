package org.apis.demootp.web.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apis.demootp.service.OtpService;
import org.apis.demootp.service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
//@RequiredArgsConstructor
public class OtpResource {
    Log logger = LogFactory.getLog(OtpResource.class);

    private final OtpService otpService;
    private String phoneNumber = "998906384606";

    private OtpDTO otpDTO;
    private OtpDateTimeDTO otpDateTimeDTO;
    private boolean used = false;

    public OtpResource(OtpService otpService) {
        otpDTO = new OtpDTO();
        this.otpService = otpService;
    }

    @GetMapping("/otp")
    public OtpDTO getOtp() {
        if (used) {
            otpDTO.setOtp("");
        }
        used = true;
        logger.info("Get OTP: " + otpDTO.getOtp() + " used: " + used);
        return otpDTO;
//        return "{\"otp\": " + otpDTO.getOtp() + "}";
    }

    @PostMapping("/otp")
    public String postParamOtp(@RequestParam String otp) {
        used = false;
        otpDTO = new OtpDTO();
        otpDTO.setOtp(otp);
        logger.info("Get OTP: " + otpDTO.getOtp() + " used: " + used);
        return "otp: " + otpDTO.getOtp();
    }

    @PostMapping("/otp2")
    public OtpResponseJson postBodyOtp(@RequestBody OtpDTO _otpDTO) {
        // Yangi OtpResponseJson obyektini yaratamiz va qaytaramiz
        used = false;
        this.otpDTO = _otpDTO;
        OtpResponseJson response = new OtpResponseJson(true, "Your OTP is valid: " + otpDTO.getOtp());
        logger.info("POST OTP: " + otpDTO.getOtp() + " used: " + used);
        return response; // Bu natija Spring tomonidan avtomatik JSON ga aylantiriladi
    }

    @GetMapping("/otpfull")
    public OtpDateTimeDTO getOtpFull() {
        if (used) {
            otpDateTimeDTO.setOtp("");
        }
        used = true;
        logger.info("Get OTP: " + otpDateTimeDTO.getOtp() + " used: " + used + " dateTime: " + otpDateTimeDTO.getDateTime());
        return otpDateTimeDTO;
    }

    @PostMapping("/otpfull2")
    public OtpDateTimeResJson postBodyOtpFull(@RequestBody OtpDateTimeDTO _otpDateTimeDTO) {
        used = false;
        this.otpDateTimeDTO = _otpDateTimeDTO;
        OtpDateTimeResJson response = new OtpDateTimeResJson(true, "Your OTP is valid: " + otpDateTimeDTO.getOtp(), otpDateTimeDTO.getDateTime());
        logger.info("POST OTP: " + otpDateTimeDTO.getOtp() + " used: " + used + " dateTime: " + otpDateTimeDTO.getDateTime());
        return response;
    }

    /// ///////////////////////////////
    @GetMapping("/get-otp")
    public CompletableFuture<ResponseEntity<ResponseToAutoTest>> getOtpAsync(RequestFromAutoTest requestFromAutoTest) {
        logger.info("PhoneNumber:  " + requestFromAutoTest.getPhoneNumber() + "; dateTime: " + requestFromAutoTest.getDateTime());
        phoneNumber = requestFromAutoTest.getPhoneNumber();

        return otpService.waiterForOtp(requestFromAutoTest.getPhoneNumber())
                .thenApply(otp -> {
                    logger.info("OTP successfully retrieved for dateTime: " + requestFromAutoTest.getPhoneNumber());
                    return ResponseEntity.ok(new ResponseToAutoTest(otp, "success"));
                })
                .exceptionally(ex -> {
                    logger.error("OTP request failed for dateTime: " + requestFromAutoTest.getPhoneNumber() + ", error: " + ex.getMessage());
                    return ResponseEntity.status(408) // Request Timeout
                            .body(new ResponseToAutoTest(null, "timeout"));
                });
    }

    @PostMapping("/otpfull")
    public ResponseEntity<String> saveOtp(@RequestBody RequestFromMobile requestFromMobile) {
        logger.info("POST: smsMessage=" + requestFromMobile.getSmsMessage() + ", time=" + requestFromMobile.getDateTime());
        otpService.submitOtp(requestFromMobile, phoneNumber);
//        return ResponseEntity.ok().body("Otp saved");
        return ResponseEntity.ok("Otp saved");
    }

}
