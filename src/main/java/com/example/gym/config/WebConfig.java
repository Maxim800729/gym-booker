package com.example.gym.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/")
                .setViewName("forward:/index.html");

        registry.addViewController("/booking")
                .setViewName("forward:/booking.html");

        registry.addViewController("/cabinet")
                .setViewName("forward:/cabinet.html");

        registry.addViewController("/admin")
                .setViewName("forward:/admin.html");
    }
}
