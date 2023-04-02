package com.example.gabojago_server.security.oauth2;

import java.util.Map;

public class KakaoMember {
    private final Map<String, Object> attributes;


    public KakaoMember(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return getAttributeByName("id");
    }

    public String getName() {
        return getProfile().get("nickname").toString();
    }

    public String getEmail() {
        return getKakaoAccount().get("email").toString();
    }

    public String getBirthday() {
        return getKakaoAccount().get("birthday").toString();
    }

    private String getAttributeByName(String attributeName) {
        return attributes.get(attributeName).toString();
    }

    private Map<String, Object> getProfile() {
        try {
            return (Map<String, Object>) getKakaoAccount().get("profile");
        } catch (ClassCastException e) {
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }

    private Map<String, Object> getKakaoAccount() {
        try {
            return (Map<String, Object>) attributes.get("kakao_account");
        } catch (ClassCastException e) {
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }


}
