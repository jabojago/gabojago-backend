package com.example.gabojago_server.model.articlecomment;

import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.common.BaseTimeEntity;
import com.example.gabojago_server.model.member.Member;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"writer", "article"})
public class ArticleComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Column(nullable = false, length = 500)
    private String content; // 댓글 내용

    @Builder
    public ArticleComment(Article article, Member writer, String content) {
        this.article = article;
        this.writer = writer;
        this.content = content;
    }

    public static ArticleComment update(ArticleComment comment, String content) {
        comment.content = content;
        return comment;
    }

    public void update(String content) {
        if (StringUtils.hasText(content)) this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
