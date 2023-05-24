package com.example.gabojago_server.service.common;

import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.article.QnaArticle;
import com.example.gabojago_server.model.articlecomment.ArticleComment;
import com.example.gabojago_server.model.like.LikeEntity;
import com.example.gabojago_server.model.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityFinder {
    private final EntityManager entityManager;

    private <T> T find(Long id, Class<T> clazz, ErrorCode errorCode) {
        T result = entityManager.find(clazz, id);
        if (result == null) throw new GabojagoException(errorCode);
        return result;
    }

    public Member findMember(Long memberId) {
        return find(memberId, Member.class, ErrorCode.MEMBER_NOT_FOUND);
    }

    public LikeEntity findLike(Long likeId) {
        return find(likeId, LikeEntity.class, ErrorCode.INVALID_LIKE);
    }

    public ArticleComment findComment(Long commentId) {
        return find(commentId, ArticleComment.class, ErrorCode.COMMENT_NOT_FOUND);
    }

    public Article findArticle(Long articleId) {
        return find(articleId, Article.class, ErrorCode.ARTICLE_NOT_FOUND);
    }

    public AccompanyArticle findAccompanyArticle(Long articleId) {
        return find(articleId, AccompanyArticle.class, ErrorCode.ARTICLE_NOT_FOUND);
    }

    public QnaArticle findQnaArticle(Long articleId) {
        return find(articleId, QnaArticle.class, ErrorCode.ARTICLE_NOT_FOUND);
    }

}
