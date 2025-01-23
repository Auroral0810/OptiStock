package com.auroral.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 根据实际需求，允许多个域名/IP 地址
                        .allowedOrigins(
                                "http://localhost:5173",     // 开发环境
                                "https://118.31.36.52",      // 服务器 IP
                                "https://yyf0810.cn",         // 域名
                                "http://localhost:3306"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
                        .allowCredentials(true);
            }
        };
    }
}
