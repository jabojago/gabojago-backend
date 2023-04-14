package com.example.gabojago_server.service.article;

import com.example.gabojago_server.config.SecurityUtil;
import com.example.gabojago_server.dto.response.article.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.PageAccompanyResponseDto;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.ArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccompanyService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public AccompanyResponseDto oneAccompany(Long id) {
        AccompanyArticle article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            article.reviewCountUp();
            return AccompanyResponseDto.of(article, false);
        } else {
            Member member = memberRepository.findById(Long.parseLong(authentication.getName())).orElseThrow();
            boolean isWritten = article.getWriter().equals(member);
            article.reviewCountUp();
            return AccompanyResponseDto.of(article, isWritten);
        }
    }

    public List<PageAccompanyResponseDto> allAccompany() {
        List<AccompanyArticle> accompanyArticles = articleRepository.findAll();
        return accompanyArticles.stream()
                .map(PageAccompanyResponseDto::of)
                .collect(Collectors.toList());
    }

    public Page<PageAccompanyResponseDto> pageAccompany(int pageNum) {
        return articleRepository.searchAll(PageRequest.of(pageNum - 1, 20));
    }

    @Transactional
    public AccompanyResponseDto postAccompany(String title, String content,
                                              String region, LocalDate startDate, LocalDate endDate,
                                              int recruitNumber) {
        Member writer = isMember();
        AccompanyArticle article = AccompanyArticle.createAccompanyArticle(writer, title, content, 0, region,
                startDate, endDate, recruitNumber);
        return AccompanyResponseDto.of(articleRepository.save(article), true);
    }

    public Member isMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberIdx())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public AccompanyArticle authorizationAccompanyWriter(Long id) {
        Member member = isMember();
        AccompanyArticle article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("글이 없습니다."));
        if (!article.getWriter().equals(member)) {
            throw new RuntimeException("로그인한 유저와 작성 유저가 같지 않습니다.");
        }
        return article;
    }

    @Transactional
    public AccompanyResponseDto changeAccompanyArticle(Long id, String title, String content,
                                                       String region, LocalDate startDate,
                                                       LocalDate endDate, int recruitMember) {
        AccompanyArticle article = authorizationAccompanyWriter(id);
        return AccompanyResponseDto.of(
                articleRepository.save(
                        AccompanyArticle.update(
                                article, title, content, region, startDate, endDate, recruitMember
                        )
                ), true
        );
    }

    @Transactional
    public void deleteAccompanyArticle(Long id) {
        AccompanyArticle article = authorizationAccompanyWriter(id);
        articleRepository.delete(article);
    }
}
