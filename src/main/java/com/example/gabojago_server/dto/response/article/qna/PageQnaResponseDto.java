package com.example.gabojago_server.dto.response.article.qna;

import com.example.gabojago_server.model.article.QnaArticle;
import com.example.gabojago_server.model.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageQnaResponseDto {
    private Long id;

    private Member writer;

    private String title;

    private String content;

    private int review;

    private boolean isSelected;

    public static PageQnaResponseDto of(QnaArticle article) {
        return PageQnaResponseDto.builder()
                .id(article.getId())
                .writer(article.getWriter())
                .title(article.getTitle())
                .content(article.getContent())
                .review(article.getReview())
                .isSelected(article.isSelected())
                .build();
    }
}
