package com.example.gabojago_server.security.filter;

import com.example.gabojago_server.dto.request.member.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static String DEFAULT_FILTER_PROCESSES_URI = "/auth/login/**";
    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_FILTER_PROCESSES_URI);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String result = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        LoginRequestDto loginRequestDto = objectMapper.readValue(result, LoginRequestDto.class);
        return loginRequestDto.toAuthentication();
    }


}
