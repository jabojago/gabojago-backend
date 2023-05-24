package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.dto.TokenDto;
import com.example.gabojago_server.dto.request.member.EmailRequestDto;
import com.example.gabojago_server.dto.request.member.LoginRequestDto;
import com.example.gabojago_server.dto.request.member.MemberRequestDto;
import com.example.gabojago_server.dto.response.NormalResponse;
import com.example.gabojago_server.dto.response.member.EmailConfirmResponse;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
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
    public ResponseEntity<EmailConfirmResponse> mailConfirm(@RequestBody EmailRequestDto requestDto) throws Exception {
        if (isAlreadyExistEmail(requestDto.getEmail())) throw new GabojagoException(ErrorCode.ALREADY_MEMBER);
        String code = signupEmailService.sendSimpleMessage(requestDto.getEmail());
        return ResponseEntity.ok(new EmailConfirmResponse(code));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.loginMember(loginRequestDto));
    }

    @PostMapping("/findPw")
    @ResponseBody
    public ResponseEntity<NormalResponse> findPw(@RequestBody EmailRequestDto requestDto) throws Exception {
        if (!isAlreadyExistEmail(requestDto.getEmail())) throw new GabojagoException(ErrorCode.MEMBER_NOT_FOUND);
        String newPassword = pwEmailService.sendSimpleMessage(requestDto.getEmail());
        authService.changeTempPw(requestDto.getEmail(), newPassword);
        return ResponseEntity.ok(NormalResponse.success());
    }

    @GetMapping("/token")
    public ResponseEntity<TokenDto> login(TokenDto tokenDto) {
        return ResponseEntity.ok(tokenDto);
    }

    private boolean isAlreadyExistEmail(String email) {
        return authService.findMember(email);
    }

}