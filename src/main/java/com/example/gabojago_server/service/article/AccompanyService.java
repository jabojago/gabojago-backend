package com.example.gabojago_server.service.article;

import com.example.gabojago_server.dto.response.article.accompany.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.accompany.PageAccompanyResponseDto;
import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.accompany.AccompanyArticleRepository;
import com.example.gabojago_server.service.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyService {
    private final AccompanyArticleRepository articleRepository;
    private final EntityFinder entityFinder;

    public AccompanyResponseDto oneAccompany(Long writerId, Long articleId) {
        AccompanyArticle article = entityFinder.findAccompanyArticle(articleId);
        article.reviewCountUp();
        if (isOwner(article, writerId)) return AccompanyResponseDto.of(article, true);
        else return AccompanyResponseDto.of(article, false);
    }

    private boolean isOwner(Article article, Long writerId) {
        return article.getWriter().getId().equals(writerId);
    }

    public Page<PageAccompanyResponseDto> allAccompany(Pageable pageable) {
        return articleRepository.searchAll(pageable);
    }

    @Transactional
    public AccompanyResponseDto postAccompany(Long writerId, String title, String content,
                                              String region, LocalDate startDate, LocalDate endDate,
                                              int recruitNumber) {
        Member writer = entityFinder.findMember(writerId);
        AccompanyArticle article = AccompanyArticle.createAccompanyArticle(writer, title, content, 0, region,
                startDate, endDate, recruitNumber);
        return AccompanyResponseDto.of(articleRepository.save(article), true);
    }

    @Transactional
    public AccompanyResponseDto changeAccompanyArticle(Long writerId, Long articleId, String title, String content,
                                                       String region, LocalDate startDate,
                                                       LocalDate endDate, int recruitMember) {
        AccompanyArticle article = authorizationAccompanyWriter(writerId, articleId);
        return AccompanyResponseDto.of(
                articleRepository.save(
                        AccompanyArticle.update(
                                article, title, content, region, startDate, endDate, recruitMember
                        )
                ), true
        );
    }

    @Transactional
    public void deleteAccompanyArticle(Long writerId, Long articleId) {
        AccompanyArticle article = authorizationAccompanyWriter(writerId, articleId);
        articleRepository.delete(article);
    }

    private AccompanyArticle authorizationAccompanyWriter(Long writerId, Long articleId) {
        Member member = entityFinder.findMember(writerId);
        AccompanyArticle article = entityFinder.findAccompanyArticle(articleId);
        if (!article.getWriter().equals(member)) {
            throw new GabojagoException(ErrorCode.FORBIDDEN_ARTICLE);
        }
        return article;
    }
}
