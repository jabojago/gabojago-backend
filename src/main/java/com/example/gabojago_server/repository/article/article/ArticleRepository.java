package com.example.gabojago_server.repository.article.article;

import com.example.gabojago_server.dto.response.article.community.PageArticleResponseDto;
import com.example.gabojago_server.model.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    @Query("SELECT a FROM Article a WHERE TYPE(a) <> AccompanyArticle AND TYPE(a) <> QnaArticle ")
    List<Article> findAllArticlesExcludingAccompanyAndQna();

    default Page<PageArticleResponseDto> searchAll(Pageable pageable) {
        List<Article> allArticles = findAllArticlesExcludingAccompanyAndQna();

        List<PageArticleResponseDto> filtered = allArticles.stream()
                .map(PageArticleResponseDto::of)
                .collect(Collectors.toList());

        int totalSize = allArticles.size();

        return new PageImpl<>(filtered, pageable, totalSize);
    }

}
