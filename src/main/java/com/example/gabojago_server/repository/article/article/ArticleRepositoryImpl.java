package com.example.gabojago_server.repository.article.article;

import com.example.gabojago_server.dto.response.article.community.PageArticleResponseDto;
import com.example.gabojago_server.model.article.Article;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.gabojago_server.model.article.QAccompanyArticle.accompanyArticle;
import static com.example.gabojago_server.model.article.QArticle.article;
import static com.example.gabojago_server.model.article.QQnaArticle.qnaArticle;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PageArticleResponseDto> searchAll(Pageable pageable) {
        List<Article> articles = jpaQueryFactory
                .selectFrom(article)
                .where(
                        article.id.ne(accompanyArticle.id),
                        article.id.ne(qnaArticle.id)
                )
                .orderBy(article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PageArticleResponseDto> pages = articles
                .stream()
                .map(PageArticleResponseDto::of)
                .collect(Collectors.toList());

        int totalSize = jpaQueryFactory
                .selectFrom(qnaArticle)
                .fetch()
                .size();

        return new PageImpl<>(pages, pageable, totalSize);
    }
}
