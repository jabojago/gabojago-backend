package com.example.gabojago_server.repository.article.accompany;

import com.example.gabojago_server.dto.response.article.PageAccompanyResponseDto;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.gabojago_server.model.article.QAccompanyArticle.accompanyArticle;

@Repository
@RequiredArgsConstructor
public class AccompanyArticleRepositoryImpl implements AccompanyArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PageAccompanyResponseDto> searchAll(Pageable pageable) {
        List<AccompanyArticle> articles = jpaQueryFactory
                .selectFrom(accompanyArticle)
                .orderBy(accompanyArticle.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PageAccompanyResponseDto> pages = articles
                .stream()
                .map(PageAccompanyResponseDto::of)
                .collect(Collectors.toList());

        int totalSize = jpaQueryFactory
                .selectFrom(accompanyArticle)
                .fetch()
                .size();

        return new PageImpl<>(pages, pageable, totalSize);
    }
}
