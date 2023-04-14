package com.example.gabojago_server.repository.articlecomment;

import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.articlecomment.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findAllByArticle(Article article);
}
