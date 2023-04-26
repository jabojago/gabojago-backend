package com.example.gabojago_server.repository.article.accompany;

import com.example.gabojago_server.model.article.AccompanyArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyArticleRepository extends JpaRepository<AccompanyArticle, Long>, AccompanyArticleRepositoryCustom {

    @Query("select count(a) > 0 from Article a where a.writer.id = :writer and  a.id = :article")
    boolean existArticleByWriter(@Param("writer") Long writerId, @Param("article") Long articleId);
}
