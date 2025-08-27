package org.apis.demootp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")  // Asosiy sahifa uchun (masalan: http://localhost:8080/)
    public String redirectToDisplayPage(){
        // Endi u mavjud "displayingOTP.html" fayliga yo'naltiradi
//        return "streaming-otps"";
        return "redirect:/streaming-otps";
    }
//    public String home(){
//        return "home";
//    }
}
