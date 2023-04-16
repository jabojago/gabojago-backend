package com.example.gabojago_server.dto.response.comment;

import com.example.gabojago_server.model.articlecomment.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;

    private String writerNickname;

    private String content;

    private boolean isWritten;

    public static CommentResponseDto of(ArticleComment comment, boolean isWritten) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .writerNickname(comment.getWriter().getNickname())
                .content(comment.getContent())
                .isWritten(isWritten)
                .build();
    }
}
