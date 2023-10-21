//package com.interswitch.Unsolorockets.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@EnableWebMvc
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//            registry.addMapping("/**")
//                    .allowCredentials(true)
//                    .allowedOriginPatterns("http://localhost:5174")
//                    .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With") //, "Access-Control-Allow-Origin")
//                    .exposedHeaders("Authorization")
//                    .allowedMethods("GET", "POST", "PUT", "DELETE")
//                    .maxAge(3600);
//
//        }
//}
//
