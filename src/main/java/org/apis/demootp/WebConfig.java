package org.apis.demootp;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configurable
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**") // Barcha yo'llar (endpoints) uchun
                .allowedOrigins("http://shuhratdev.uz", "http:/www.shuhratdev.uz") // Faqat shu domenlardan kelgan so'rovlarga ruxsat
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Ruxsat etilgan methodlar
                .allowedHeaders("*") // Barcha headerlarga ruxsat
                .allowCredentials(false); // Cookie'lar
    }
}
