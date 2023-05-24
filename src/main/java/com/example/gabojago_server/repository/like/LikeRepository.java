package com.example.gabojago_server.repository.like;

import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.like.LikeEntity;
import com.example.gabojago_server.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByArticleAndMember(Article article, Member member);

    @Query(" select count(*) from LikeEntity l where l.article = :article ")
    Integer countOfArticle(@Param("article") Article article);
}
