package com.example.gabojago_server.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
@Slf4j
public class SecurityUtil {

    public static Long getCurrentMemberIdx() {
        //JwtAuthenticationFilter에서 저장한 유저 정보 꺼내기
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return 0L;
        }

        //userIdx return
        return Long.parseLong(authentication.getName());
    }
}
