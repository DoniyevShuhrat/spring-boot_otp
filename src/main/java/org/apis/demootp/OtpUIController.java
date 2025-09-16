package org.apis.demootp;

import org.apis.demootp.service.dto.RequestFromMobile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class OtpUIController {

    // Agar ma'lumotlarni Service'dan olmoqchi bo'lsangiz
    // private final OtpService otpService;
    //
    // public OtpUIController(OtpService otpService) {
    //     this.otpService = otpService;
    // }

    @GetMapping("/display-otps") // Brouzerda qauso URL'da ochilishini belgilaydi
    public String showOtpPage(Model model) {
        // --- 1. Ma'lumotlarni tayyorlash ---
        // Hozircha test uchun o'zimiz ma'lumot yaratamiz.
        // Aslida bu yerda siz o'zingizni OtpService'ingizni chaqirishingiz kerak bo'ladi
        // Exp: List<RequestFromMobile> testOtps = otpService.getTestOtps();

        List<RequestFromMobile> testOtps = Arrays.asList(
                new RequestFromMobile("28.08.2025", "OTP code: 123456"),
                new RequestFromMobile("28.08.2025", "OTP code: 123457")
        );

        // --- 2. Ma'lumotlani Model'ga qo'shish ---
        // "otps" - bu nom bilan HTML'da ma'lumotlarni chaqirib olamiz.
        model.addAttribute("otps", testOtps);

        // --- 3. Qaysi HTML faylni ko'rsatish kerakligini aytish ---
        // Bu "src/main/resources/templates/displayingOTP.html" faylini anglatadi.

        return "displayingOTP";
    }

    @GetMapping("/streaming-otps") // Yangi sahifamiz uchun yangi URL
    public String showStreamingOtpPage(Model model) {
        // Sahifa birinchi marta ochilganda bo'sh bo'lishi mumkin.
        // Agar boshlang'ich ma'lumotlar bo'lishini xohlasangiz,
        // bu yerda service'dan ma'lumotlarni olib, model'ga qo'shishingiz mumkin.
        // Hozircha bo'sh ro'yxat jo'natamiz, chunki ma'lumotlar jonli ravishda keladi.
        model.addAttribute("otps", List.of()); // Boshlang'ich holatda jadval bo'sh bo'ladi

        // Bu "src/main/resources/templates/displayingStreamingOtp.html" faylini anglatadi
        return "displayingStreamingOtp";
    }


    /*
     * Eng To'g'ri va Zamonaviy Usul: Server-Sent Events (SSE)
     * Qanday ishlaydi: Brauzer serverga bitta uzoq muddatli so'rov bilan ulanadi. Server esa o'zida qachon yangi ma'lumot paydo bo'lsa, o'sha aloqa kanali orqali brauzerga ma'lumotni "itarib" (push) yuboradi. Aloqa bir tomonlama (server -> brauzer).
     * Afzalligi: Sizning vazifangiz uchun (serverdagi o'zgarishni ko'rsatish) ideal yechim. WebSockets'ga qaraganda ancha sodda va samarali.
     * Kamchiligi: Aloqa faqat bir tomonlama. Brauzer serverga shu kanal orqali ma'lumot yubora olmaydi.
     */
    // Barcha ulangan klientlar uchun emitter'larni saqlab turadigan ro'yxat.
    // CopyOnWriteArrayList ko'p oqimli (multi-threaded) muhitda xavfsiz ishlaydi.
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Bu endpoint SSE ulanishlarini qabul qiladi
    @GetMapping("/otp-stream")
    public SseEmitter streamOtps() {
        // Timeout'ni aniqroq belgilaymiz (masalan, 30 daqiqa)
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Ulanish vaqtini cheksiz qilamiz

        // Yangi emitter'ni ro'yxatga qo'shamiz
        this.emitters.add(emitter);

        // Ulanish uzilganida yoki xatolik bo'lganida emitter'ni ro'yxatdan o'chiramiz
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        emitter.onError((error) -> this.emitters.remove(emitter));

        // --- MUHIM QO'SHIMCHA ---
        // Ulanish o'rnatilishi bilan darhol birinchi xabarni yuboramiz
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connection established"));
        } catch (IOException e) {
            // Bu emitter'ni o'chiramiz, chunki unga ma'lumot yuborib bo'lmadirectories
            this.emitters.remove(emitter);
        }
        // ---------------------------------

        return emitter;
    }

    public void sendOtpUpdate(RequestFromMobile newOtp) {
        for (SseEmitter emitter : this.emitters) {
            try {
                // Har bir ulangan klientga yangi OTP ma'lumotini JSON formatida yuboramiz
                emitter.send(SseEmitter.event().data(newOtp));
            } catch (IOException e) {
                // Xatolik bo'lgan ulangan client'i ro'yxatdan o'chiramiz
                this.emitters.remove(emitter);
            }
        }
    }
}
