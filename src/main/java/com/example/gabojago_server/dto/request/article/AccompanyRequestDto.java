package com.example.gabojago_server.dto.request.article;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccompanyRequestDto {
    private String title;

    private String content;

    private String region;

    private LocalDate startDate;

    private LocalDate endDate;

    private int recruitNumber;
}
