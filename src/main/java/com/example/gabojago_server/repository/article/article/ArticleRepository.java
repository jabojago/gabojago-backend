package com.example.gabojago_server.repository.article.article;

import com.example.gabojago_server.model.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.id = :articleId and a.writer.id = :writerId")
    Optional<Article> findByWriterAndArticle(@Param("writerId") Long writerId, @Param("articleId") Long articleId);
}
