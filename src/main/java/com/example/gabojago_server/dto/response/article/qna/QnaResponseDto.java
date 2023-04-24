package com.example.gabojago_server.dto.response.article.qna;

import com.example.gabojago_server.model.article.QnaArticle;
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

    private boolean selected;

    private boolean isWritten;

    public static QnaResponseDto of(QnaArticle article, boolean isWritten) {
        return QnaResponseDto.builder()
                .id(article.getId())
                .writer(article.getWriter())
                .title(article.getTitle())
                .content(article.getContent())
                .review(article.getReview())
                .selected(article.selected())
                .isWritten(isWritten)
                .build();
    }
}
