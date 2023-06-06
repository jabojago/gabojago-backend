package com.example.gabojago_server.service.member;

import com.example.gabojago_server.config.SecurityUtil;
import com.example.gabojago_server.dto.response.member.MemberInfoResponseDto;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.member.MemberRepository;
import com.example.gabojago_server.service.common.EntityFinder;
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
    private final EntityFinder entityFinder;

    //header의 token 값을 토대로 member의 data 반환
    public MemberInfoResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberIdx())
                .map(MemberInfoResponseDto::of)
                .orElseThrow(() -> new GabojagoException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public MemberResponseDto changeNickname(Long id, String nickname) {
        if (memberRepository.existsByNickname(nickname)) throw new GabojagoException(ErrorCode.ALREADY_MEMBER);
        Member member = entityFinder.findMember(id);
        member.updateNickName((nickname));
        return MemberResponseDto.of(member);
    }

    @Transactional
    public MemberResponseDto changePassword(Long id, String newPassword) {
        Member member = entityFinder.findMember(id);
        member.updatePassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDto.of(member);
    }

    @Transactional
    public MemberResponseDto changePhone(Long id, String phone) {
        Member member = entityFinder.findMember(id);
        member.updatePhone((phone));
        return MemberResponseDto.of(member);
    }
}