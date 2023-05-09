package com.example.gabojago_server.service.article;

import com.example.gabojago_server.dto.response.article.community.ArticleResponseDto;
import com.example.gabojago_server.dto.response.article.community.OneArticleResponseDto;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.article.ArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Page<ArticleResponseDto> allArticle(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleResponseDto::from);
    }

    public OneArticleResponseDto oneArticle(Long memberId, Long articleId) {
        boolean isWritten = false;
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalStateException::new);
        article.reviewCountUp();
        if (article.getWriter().getId().equals(memberId)) isWritten = true;
        return OneArticleResponseDto.builder()
                .articleId(articleId)
                .content(article.getContent())
                .review(article.getReview())
                .isWritten(isWritten)
                .nickname(article.getWriter().getNickname())
                .title(article.getTitle())
                .build();
    }

    @Transactional
    public ArticleResponseDto postArticle(Long writerId, String title, String content) {
        Member writer = findWriter(writerId);
        Article article = Article.builder()
                .content(content)
                .title(title)
                .writer(writer)
                .build();
        return ArticleResponseDto.from(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponseDto changeArticle(Long writerId, Long articleId, String title, String content) {
        Article article = findArticle(articleId);
        Member writer = findWriter(writerId);
        if (!article.getWriter().equals(writer)) throw new IllegalStateException();// 권한
        article.edit(title, content);
        return ArticleResponseDto.from(article);
    }

    @Transactional
    public void deleteArticle(Long writerId, Long articleId) {
        Article article = findArticle(articleId);
        Member writer = findWriter(writerId);
        if (!article.getWriter().equals(writer)) return;
        articleRepository.delete(article);
    }

    private Member findWriter(Long writerId) {
        return memberRepository.findById(writerId).orElseThrow(IllegalStateException::new);
    }

    private Article findArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(IllegalStateException::new);
    }

}
