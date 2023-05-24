package com.example.gabojago_server.service.like;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.article.ArticleRepository;
import com.example.gabojago_server.repository.like.LikeRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import com.example.gabojago_server.service.common.EntityFinder;
import com.example.gabojago_server.steps.ArticleStep;
import com.example.gabojago_server.steps.MemberStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({LikeService.class,
        JpaConfig.class,
        EntityFinder.class
})
class LikeServiceTest {

    @Autowired
    private LikeService likeService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LikeRepository likeRepository;

    private MemberStep memberStep;
    private ArticleStep articleStep;


    @BeforeEach
    void setup() {
        memberStep = new MemberStep(memberRepository);
        articleStep = new ArticleStep(articleRepository);
    }

    @Test
    public void 게시글에_좋아요_누르기() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        Article article = articleStep.createDefault(writer);

        // when
        likeService.clickLike(article.getId(), writer.getId());

        // then
        assertThat(likeRepository.findByArticleAndMember(article, writer))
                .isPresent();
    }

    @Test
    public void 게시글_좋아요_수_조회() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        Article article = articleStep.createDefault(writer);
        likeService.clickLike(article.getId(), writer.getId());

        // when
        int likeNumber = likeService.getLike(article.getId());

        // then

        assertThat(likeNumber).isEqualTo(1);
    }

    @Test
    public void 게시글_좋아요_수_조회_중복_좋아요_불가() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        Article article = articleStep.createDefault(writer);
        likeService.clickLike(article.getId(), writer.getId());
        likeService.clickLike(article.getId(), writer.getId());

        // when
        int likeNumber = likeService.getLike(article.getId());

        // then

        assertThat(likeNumber).isEqualTo(1);
    }

    @Test
    public void 동행_게시글에_좋아요() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        AccompanyArticle article = articleStep.createAccompany(writer);
        // when
        likeService.clickLike(article.getId(), writer.getId());
        // then

        assertThat(likeRepository.findByArticleAndMember(article, writer))
                .isPresent();
    }

}