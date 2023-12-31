package com.wisetechglobal.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExerciseApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200","http://localhost:3000")
                        .allowedMethods(HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.PUT.name(),HttpMethod.DELETE.name());
            }
        };
    }
}
