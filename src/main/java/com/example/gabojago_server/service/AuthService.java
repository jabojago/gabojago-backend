package com.example.gabojago_server.service;

import com.example.gabojago_server.dto.LoginRequestDto;
import com.example.gabojago_server.dto.MemberRequestDto;
import com.example.gabojago_server.dto.MemberResponseDto;
import com.example.gabojago_server.dto.TokenDto;
import com.example.gabojago_server.model.Member;
import com.example.gabojago_server.repository.MemberRepository;
import com.example.gabojago_server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberResponseDto joinMember(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = requestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    public TokenDto loginMember(LoginRequestDto requestDto) {
        //email, pw를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        //pw 체크하고 사용자가 맞는지 검증
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        //인증정보 기반으로 토큰 생성해 return
        return jwtTokenProvider.createToken(authentication);
    }

}