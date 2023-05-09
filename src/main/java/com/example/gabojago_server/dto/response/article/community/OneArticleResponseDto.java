package com.example.gabojago_server.dto.response.article.community;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OneArticleResponseDto {
    private Long articleId;
    private String nickname;
    private String title;
    private String content;
    private int review;
    private boolean isWritten;

    @Builder
    public OneArticleResponseDto(Long articleId, String nickname, String title, String content, int review, boolean isWritten) {
        this.articleId = articleId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.review = review;
        this.isWritten = isWritten;
    }


}
