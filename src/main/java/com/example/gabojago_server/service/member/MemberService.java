package com.example.gabojago_server.service.member;

import com.example.gabojago_server.config.SecurityUtil;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //header의 token 값을 토대로 member의 data 반환
    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberIdx())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public MemberResponseDto changeNickname(Long id, String nickname) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.updateNickName((nickname));
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changePassword(Long id,  String newPassword) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.updatePassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto changePhone(Long id, String phone) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.updatePhone((phone));
        return MemberResponseDto.of(memberRepository.save(member));
    }
}
