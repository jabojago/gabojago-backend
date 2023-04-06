package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.common.BaseTimeEntity;
import com.example.gabojago_server.model.member.Member;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(exclude = "writer")
public abstract class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Member writer;

    @Column(nullable = false)
    protected String title; //제목

    @Column(nullable = false, length = 2000)
    protected String content; // 내용

    protected int review; //조회수

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
