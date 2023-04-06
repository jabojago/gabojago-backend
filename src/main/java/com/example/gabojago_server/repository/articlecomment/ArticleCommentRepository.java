package com.example.gabojago_server.repository.articlecomment;

import com.example.gabojago_server.model.articlecomment.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
