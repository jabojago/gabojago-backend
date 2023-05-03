package com.example.gabojago_server.dto.response.article.qna;

import com.example.gabojago_server.model.article.QnaArticle;
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

    private String nickname;

    private String title;

    private String content;

    private int review;

    private boolean selected;

    private boolean isWritten;

    public static QnaResponseDto of(QnaArticle article, boolean isWritten) {
        return QnaResponseDto.builder()
                .id(article.getId())
                .nickname(article.getWriter().getNickname())
                .title(article.getTitle())
                .content(article.getContent())
                .review(article.getReview())
                .selected(article.isSelected())
                .isWritten(isWritten)
                .build();
    }
}
