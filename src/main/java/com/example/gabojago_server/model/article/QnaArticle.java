package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.common.BooleanToYNConverter;
import com.example.gabojago_server.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("QnaArticle")
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaArticle extends Article {

    @Column(name = "selected")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean selected;

    public static QnaArticle createQnaArticle(Member writer, String title, String content,
                                              int review, boolean selected) {
        QnaArticle article = new QnaArticle();
        article.writer = writer;
        article.title = title;
        article.content = content;
        article.review = review;
        article.selected = selected;
        return article;
    }

    public static QnaArticle update(QnaArticle article, String title, String content,
                                    boolean selected) {
        article.title = title;
        article.content = content;
        article.selected = selected;
        return article;
    }

}