package com.example.gabojago_server.steps;

import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.article.ArticleRepository;

import static java.time.LocalDate.now;

public class ArticleStep {
    private final ArticleRepository articleRepository;

    public ArticleStep(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createDefault(Member writer) {
        return articleRepository.save(
                Article.builder()
                        .writer(writer)
                        .title("제목")
                        .content("제목")
                        .build());
    }

    public AccompanyArticle createAccompany(Member writer) {
        AccompanyArticle article = AccompanyArticle.createAccompanyArticle(
                writer, "제목", "내용",
                1, "서울", now(), now(),
                2
        );

        return (AccompanyArticle) articleRepository.save(article);
    }

    public static AccompanyArticle createAccompanyArticleDefaults(Member writer) {
        return AccompanyArticle.createAccompanyArticle(
                writer, "제목", "내용",
                1, "서울", now(), now(),
                2
        );
    }
}
