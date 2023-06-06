package com.example.gabojago_server.dto.response.member;

import com.example.gabojago_server.model.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoResponseDto {
    private String email;

    private String nickname;

    private String birth;

    private String phone;

    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .phone(member.getPhone())
                .build();
    }
}
