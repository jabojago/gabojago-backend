package com.example.gabojago_server.repository.article.Qna;

import com.example.gabojago_server.model.article.QnaArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaArticleRepository extends JpaRepository<QnaArticle, Long>, QnaArticleRepositoryCustom {
}
