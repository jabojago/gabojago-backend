package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.dto.TokenDto;
import com.example.gabojago_server.dto.request.member.EmailRequestDto;
import com.example.gabojago_server.dto.request.member.LoginRequestDto;
import com.example.gabojago_server.dto.request.member.MemberRequestDto;
import com.example.gabojago_server.dto.response.NormalResponse;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.service.mail.ChangePwEmailService;
import com.example.gabojago_server.service.mail.SignupEmailService;
import com.example.gabojago_server.service.member.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final SignupEmailService signupEmailService;

    private final ChangePwEmailService pwEmailService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.joinMember(requestDto));
    }

    @PostMapping("/signup/mailConfirm")
    @ResponseBody
    public ResponseEntity<String> mailConfirm(@RequestBody EmailRequestDto requestDto) throws Exception {
        if (authService.findMember(requestDto.getEmail()))
            return ResponseEntity.ok(NormalResponse.duplicated().getStatus());
        String code = signupEmailService.sendSimpleMessage(requestDto.getEmail());
        return ResponseEntity.ok(code);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.loginMember(loginRequestDto));
    }

    @PostMapping("/findPw")
    @ResponseBody
    public ResponseEntity<NormalResponse> findPw(@RequestBody EmailRequestDto requestDto) throws Exception {
        if (authService.findMember(requestDto.getEmail())) {
            String newPassword = pwEmailService.sendSimpleMessage(requestDto.getEmail());
            authService.changeTempPw(requestDto.getEmail(), newPassword);
            return ResponseEntity.ok(NormalResponse.success());
        } else {
            return ResponseEntity.ok(NormalResponse.fail());
        }
    }

    @GetMapping("/token")
    public ResponseEntity<TokenDto> login(TokenDto tokenDto) {
        return ResponseEntity.ok(tokenDto);
    }


}