package com.example.gabojago_server.steps;

import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.member.MemberRepository;

public class MemberStep {

    public final MemberRepository memberRepository;

    public MemberStep(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createDefault() {
        return memberRepository.save(Member.builder()
                .email("test@test.com")
                .name("test")
                .nickname("test")
                .birth("1997-07-16")
                .build());
    }
}
