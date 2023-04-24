package com.example.gabojago_server.dto.response.article;

import com.example.gabojago_server.model.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaResponseDto {
    private Long id;

    private Member writer;

    private String title;

    private String content;

    private int review;

    private boolean isSelected;

    private boolean isWritten;


}
