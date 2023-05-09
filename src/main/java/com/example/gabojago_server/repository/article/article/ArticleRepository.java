package com.example.gabojago_server.repository.article.article;

import com.example.gabojago_server.model.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
