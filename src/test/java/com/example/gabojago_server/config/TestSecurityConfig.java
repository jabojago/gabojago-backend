package com.example.gabojago_server.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic().disable()  //rest api이므로 기본 설정 사용 x(https만 사용)
                .csrf().disable()   //rest api이므로 csrf 보안 사용 x
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/auth/signup").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/api/accompany/**").permitAll()
                .antMatchers("/api/qna/**").permitAll()
                .antMatchers("/api/articles/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .antMatchers("/api/like/**").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
