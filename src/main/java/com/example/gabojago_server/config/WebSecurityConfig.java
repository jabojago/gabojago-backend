package com.example.gabojago_server.config;

import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.security.oauth2.OAuth2CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Component
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2CustomerService oAuth2CustomerService;

    //회원 패스워드 암호화해서 저장해야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().configurationSource(corsConfigurationSource());

        http.httpBasic().disable()  //rest api이므로 기본 설정 사용 x(https만 사용)
                .csrf().disable()   //rest api이므로 csrf 보안 사용 x
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/auth/signup/**").permitAll()
                .antMatchers("/auth/findPw/**").permitAll()
                .antMatchers("/auth/login/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/api/accompany/**").permitAll()
                .antMatchers("/api/qna/**").permitAll()
                .antMatchers("/api/articles/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .antMatchers("/api/like/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));
        http.oauth2Login().defaultSuccessUrl("/auth/token").userInfoEndpoint().userService(oAuth2CustomerService);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
