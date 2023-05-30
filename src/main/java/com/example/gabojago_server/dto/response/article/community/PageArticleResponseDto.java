package com.example.gabojago_server.dto.response.article.community;

import com.example.gabojago_server.model.article.Article;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageArticleResponseDto {
    private Long id;

    private String nickname;

    private String title;

    private String content;

    private int review;


    public static PageArticleResponseDto of(Article article) {
        return PageArticleResponseDto.builder()
                .id(article.getId())
                .nickname(article.getWriter().getNickname())
                .title(article.getTitle())
                .content(article.getContent())
                .review(article.getReview())
                .build();
    }
}
