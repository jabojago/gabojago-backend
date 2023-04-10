package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.member.Member;
import lombok.*;

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

    @Builder
    public AccompanyArticle(Member writer, String title,
                            String content, String region,
                            LocalDate startDate, LocalDate endDate,
                            int recruitNumber) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.region = region;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recruitNumber = recruitNumber;
    }
}
