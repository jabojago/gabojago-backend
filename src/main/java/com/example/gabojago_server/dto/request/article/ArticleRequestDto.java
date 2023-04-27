package com.example.gabojago_server.dto.request.article;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleRequestDto {
    String title;
    String content;
}
