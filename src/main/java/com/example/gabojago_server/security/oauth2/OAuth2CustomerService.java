package com.example.gabojago_server.security.oauth2;

import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.member.Authority;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2CustomerService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        KakaoMember kakaoMember = new KakaoMember(oAuth2User.getAttributes());

        if (!memberRepository.existsByEmail(kakaoMember.getEmail())) createAndSave(kakaoMember);
        Member member = findMember(kakaoMember.getEmail());

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), Map.of("id", member.getId()), "id");
    }

    private Member findMember(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new GabojagoException(ErrorCode.KAKAO_LOGIN, "카카오 정보로 회원 정보를 가져올 수 없습니다."));
    }

    private Member createAndSave(KakaoMember kakaoMember) {
        Member member = Member.builder()
                .authority(Authority.ROLE_USER)
                .birth(kakaoMember.getBirthday())
                .email(kakaoMember.getEmail())
                .nickname(kakaoMember.getEmail())
                .name(kakaoMember.getName())
                .build();
        return memberRepository.save(member);
    }

}
