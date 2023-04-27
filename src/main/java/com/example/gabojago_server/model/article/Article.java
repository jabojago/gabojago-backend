package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.articlecomment.ArticleComment;
import com.example.gabojago_server.model.common.BaseTimeEntity;
import com.example.gabojago_server.model.member.Member;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(exclude = "writer")
@Table(name = "articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id")
    protected Member writer;

    @Column(nullable = false)
    protected String title; //제목

    @Column(nullable = false, length = 2000)
    protected String content; // 내용

    protected int review; //조회수

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    protected List<ArticleComment> commentList = new ArrayList<>();

    @Builder
    public Article(Member writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public void edit(String title, String content) {
        if (StringUtils.hasText(title)) this.title = title;
        if (StringUtils.hasText(content)) this.content = content;
    }

    public void reviewCountUp() {
        this.review++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return this.getId() != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
