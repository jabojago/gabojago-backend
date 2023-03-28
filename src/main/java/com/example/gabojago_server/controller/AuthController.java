package com.example.gabojago_server.controller;

import com.example.gabojago_server.dto.LoginRequestDto;
import com.example.gabojago_server.dto.MemberRequestDto;
import com.example.gabojago_server.dto.MemberResponseDto;
import com.example.gabojago_server.dto.TokenDto;
import com.example.gabojago_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.joinMember(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.loginMember(requestDto));
    }
}