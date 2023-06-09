package com.example.gabojago_server.dto.response.article.accompany;

import com.example.gabojago_server.model.article.AccompanyArticle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccompanyResponseDto {
    private Long id;

    private String nickname;

    private String title;

    private String content;

    private int review;

    private String region;

    private String startDate;

    private String endDate;

    private int recruitMember;

    private boolean isWritten;

    public static AccompanyResponseDto of(AccompanyArticle article, boolean isWritten) {
        return AccompanyResponseDto.builder()
                .id(article.getId())
                .nickname(article.getWriter().getNickname())
                .title(article.getTitle())
                .content(article.getContent())
                .review(article.getReview())
                .region(article.getRegion())
                .startDate(article.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .endDate(article.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .recruitMember(article.getRecruitNumber())
                .isWritten(isWritten)
                .build();

    }
}
