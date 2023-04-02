package com.example.gabojago_server.jwt;

import com.example.gabojago_server.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final long validityMsec = 1000L * 60 * 60;    //1시간만 유효
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성
    public TokenDto createToken(Authentication authentication) {

        //권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityMsec);

        //access token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())   //user idx가 지정될 것
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)   //발급시간
                .setExpiration(expiration)  //만료시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        //token dtd에 access token 정보 담기
        return TokenDto.builder()
                .token(accessToken)
                .build();
    }

    //토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        //토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //claims에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체에 토큰 정보와 생성한 인가 넣고 return
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        //확인 위해 아래와 같이 작성
        try {
            Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
        //return !claims.getBody().getExpiration().before(new Date());
    }

    private Claims parseClaims(String accessToken) {
        try {
            //올바른 토큰이면 true
            return Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            //만료 토큰이어도 토큰 정보 꺼내서 return
            return e.getClaims();
        }
    }
}
