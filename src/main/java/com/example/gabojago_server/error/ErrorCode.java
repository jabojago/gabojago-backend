package com.example.gabojago_server.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾지 못했습니다.", "요청한 게시글이 유효한지 확인해주세요"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾지 못했습니다.", "email 과 password 를 올바르게 입력했는지 확인해주세요"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 정보를 찾지 못했습니다.", "댓글 정보를 확인해주세요"),
    INVALID_LIKE(HttpStatus.NOT_FOUND, "좋아요에 대한 정보가 없습니다.", "좋아요에 대한 정보를 확인해주세요 혹은 서버로 문의바랍니다."),
    UNSUPPORTED_ALARM(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 알람 기능입니다.", "알림 기능을 지원하지 않는 호출입니다."),
    KAKAO_LOGIN(HttpStatus.UNAUTHORIZED, "카카오 로그인 오류입니다.", "카카오 로그인을 확인해주세요"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "email 또는 비밀번호가 맞지 않습니다.", "다른 이메일을 사용해야합니다."),
    FORBIDDEN_ARTICLE(HttpStatus.FORBIDDEN, "게시글에 수정, 삭제에 대한 권한이 없습니다.", "잘못된 접근입니다. 입력값을 확인해주세요."),
    FORBIDDEN_COMMENT(HttpStatus.FORBIDDEN, "댓글에 수정, 삭제에 대한 권한이 없습니다.", "잘못된 접근입니다. 입력값을 확인해주세요."),
    ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 유저 정보입니다.", "다른 이메일 혹은 닉네임을 사용해야합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됬거나 권한이 없습니다.", "토큰을 재발급 받아야합니다.")
    // HttpStatus 와 Message 을 입력하고 확장
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String solution;
}
