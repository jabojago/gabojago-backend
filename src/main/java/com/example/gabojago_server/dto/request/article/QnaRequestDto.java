package com.example.gabojago_server.dto.request.article;

import lombok.Getter;

@Getter
public class QnaRequestDto {
    private String title;

    private String content;

    private boolean selected;
}
