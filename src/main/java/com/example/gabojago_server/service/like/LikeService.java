package com.example.gabojago_server.service.like;

import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.like.LikeEntity;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.article.ArticleRepository;
import com.example.gabojago_server.repository.like.LikeRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


    public void clickLike(Long articleId, Long memberId) {
        Article article = getArticle(articleId);
        Member member = getMember(memberId);

        if (likeRepository.findByArticleAndMember(article, member).isPresent()) return;

        likeRepository.save(LikeEntity.builder()
                .article(article)
                .member(member)
                .build());
    }

    public int getLike(Long articleId) {
        Article article = getArticle(articleId);
        return likeRepository.findByArticle(article);
    }


    private Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(IllegalStateException::new);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(IllegalStateException::new);
    }

}
