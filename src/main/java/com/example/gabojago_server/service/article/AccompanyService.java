package com.example.gabojago_server.service.article;

import com.example.gabojago_server.dto.response.article.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.PageAccompanyResponseDto;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.accompany.AccompanyArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccompanyService {
    private final AccompanyArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public AccompanyResponseDto oneAccompany(Long writerId, Long articleId) {
        AccompanyArticle article = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        article.reviewCountUp();
        if (isOwner(writerId, articleId)) return AccompanyResponseDto.of(article, false);
        else return AccompanyResponseDto.of(article, true);
    }

    private boolean isOwner(Long writerId, Long articleId) {
        if (writerId == null) return false;
        return articleRepository.existArticleByWriter(writerId, articleId);
    }

    public Page<PageAccompanyResponseDto> allAccompany(Pageable pageable) {
        return articleRepository
                .findAll(pageable)
                .map(PageAccompanyResponseDto::of);
    }

    @Transactional
    public AccompanyResponseDto postAccompany(Long writerId, String title, String content,
                                              String region, LocalDate startDate, LocalDate endDate,
                                              int recruitNumber) {
        Member writer = memberRepository.findById(writerId).orElseThrow(IllegalStateException::new);
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
        Member member = memberRepository.findById(writerId).orElseThrow(IllegalStateException::new);
        AccompanyArticle article = (AccompanyArticle) articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        if (!article.getWriter().equals(member)) {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
        return article;
    }
}
