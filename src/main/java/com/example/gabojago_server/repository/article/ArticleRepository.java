package com.example.gabojago_server.repository.article;

import com.example.gabojago_server.model.article.AccompanyArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<AccompanyArticle, Long>, ArticleRepositoryCustom {
}
