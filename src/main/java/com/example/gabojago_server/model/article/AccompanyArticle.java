package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;


@Entity
@Getter
@DiscriminatorValue("AccompanyArticle")
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccompanyArticle extends Article {

    private String region; // 지역

    @Column(name = "start_date")
    private LocalDate startDate; // 동행 시작 날짜

    @Column(name = "end_date")
    private LocalDate endDate; // 동행 마지막 날짜

    private int recruitNumber; // 모집인원

    public static AccompanyArticle createAccompanyArticle(Member writer, String title,
                                                          String content, int review, String region,
                                                          LocalDate startDate, LocalDate endDate,
                                                          int recruitNumber) {
        AccompanyArticle article = new AccompanyArticle();
        article.writer = writer;
        article.title = title;
        article.content = content;
        article.review = review;
        article.region = region;
        article.startDate = startDate;
        article.endDate = endDate;
        article.recruitNumber = recruitNumber;
        return article;
    }

    public static AccompanyArticle update(AccompanyArticle article, String title, String content, String region,
                                          LocalDate startDate, LocalDate endDate,
                                          int recruitNumber) {
        article.title = title;
        article.content = content;
        article.region = region;
        article.startDate = startDate;
        article.endDate = endDate;
        article.recruitNumber = recruitNumber;
        return article;
    }
}
