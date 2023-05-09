package com.example.gabojago_server.dto.response.article.community;


import com.example.gabojago_server.model.article.Article;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleResponseDto {
    private Long articleId;
    private String nickname;
    private String title;
    private String content;
    private int review;


    @Builder
    private ArticleResponseDto(Long articleId, String nickname, String title, String content, int review) {
        this.articleId = articleId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.review = review;
    }

    public static ArticleResponseDto from(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .content(article.getContent())
                .title(article.getTitle())
                .review(article.getReview())
                .nickname(article.getWriter().getNickname())
                .build();
    }
}