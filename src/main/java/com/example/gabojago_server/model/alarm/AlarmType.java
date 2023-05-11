package com.example.gabojago_server.model.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    COMMENT("댓글"),
    LIKE("좋아요"),
    ;

    private final String message;

    public String message() {
        return this.getMessage() + "을/를 남겼습니다";
    }

}
