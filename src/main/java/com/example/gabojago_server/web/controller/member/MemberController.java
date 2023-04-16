package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.dto.request.member.ChangeNickNameRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePasswordRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePhoneRequestDto;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myInfo")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfo();
        System.out.println(myInfoBySecurity.getNickname());
        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }

    @PostMapping("/nickname")
    public ResponseEntity<MemberResponseDto> setMemberNickname(@RequestBody @Valid ChangeNickNameRequestDto request) {
        return ResponseEntity.ok(memberService.changeNickname(request.getEmail(), request.getNickname()));
    }

    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        return ResponseEntity.ok(memberService.changePassword(request.getEmail(), request.getExPassword(), request.getNewPassword()));
    }

    @PostMapping("/phone")
    public ResponseEntity<MemberResponseDto> setMemberPhone(@RequestBody @Valid ChangePhoneRequestDto request) {
        return ResponseEntity.ok(memberService.changePhone(request.getEmail(), request.getPhone()));
    }


}