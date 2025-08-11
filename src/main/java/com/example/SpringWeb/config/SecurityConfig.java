package com.example.SpringWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔은 허용
                        .anyRequest().authenticated()                  // 나머지는 인증 필요
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")     // H2 콘솔은 CSRF 예외 처리
                )
                .headers(headers -> headers.frameOptions().disable()
                )
                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 사용

        return http.build();
    }
}