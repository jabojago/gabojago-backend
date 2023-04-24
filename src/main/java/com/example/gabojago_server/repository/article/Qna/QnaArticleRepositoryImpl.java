package com.example.gabojago_server.repository.article.Qna;

import com.example.gabojago_server.dto.response.article.qna.PageQnaResponseDto;
import com.example.gabojago_server.model.article.QnaArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.gabojago_server.model.article.QQnaArticle.qnaArticle;

@Repository
@RequiredArgsConstructor
public class QnaArticleRepositoryImpl implements QnaArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PageQnaResponseDto> searchAll(Pageable pageable) {
        List<QnaArticle> articles = jpaQueryFactory
                .selectFrom(qnaArticle)
                .orderBy(qnaArticle.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PageQnaResponseDto> pages = articles
                .stream()
                .map(PageQnaResponseDto::of)
                .collect(Collectors.toList());

        int totalSize = jpaQueryFactory
                .selectFrom(qnaArticle)
                .fetch()
                .size();

        return new PageImpl<>(pages, pageable, totalSize);
    }
}
