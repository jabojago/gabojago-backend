package com.example.gabojago_server.dto.request.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaRequestDto {
    private String title;

    private String content;

    private boolean selected;
}
