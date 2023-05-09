package com.example.gabojago_server.service.article;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.dto.response.article.community.ArticleResponseDto;
import com.example.gabojago_server.dto.response.article.community.OneArticleResponseDto;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.article.ArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import com.example.gabojago_server.steps.MemberStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, ArticleService.class})
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberStep memberStep;

    @BeforeEach
    void setup() {
        memberStep = new MemberStep(memberRepository);
    }

    @Test
    public void 커뮤니티_글_등록() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        String title = "[후기] 일본 갔다온 후기입니다.";
        String content = "재미있었습니다.";

        // when
        ArticleResponseDto response = articleService.postArticle(writer.getId(), title, content);

        // then
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    public void 커뮤니티_글_수정() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        String title = "[후기] 일본 갔다온 후기입니다.";
        String content = "재미있었습니다.";
        ArticleResponseDto response = articleService.postArticle(writer.getId(), title, content);
        String changeTitle = "[후기][재업로드] 일본 갔다온 후기입니다";
        String changeContent = "[후기][재업로드] 너무 재미있었습니다.";

        // when
        ArticleResponseDto changeArticle =
                articleService.changeArticle(writer.getId(), response.getArticleId(), changeTitle, changeContent);

        // then
        assertThat(changeArticle.getTitle()).isEqualTo(changeTitle);
        assertThat(changeArticle.getContent()).isEqualTo(changeContent);
    }


    @Test
    public void 커뮤니티_글_삭제() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        String title = "[후기] 일본 갔다온 후기입니다.";
        String content = "재미있었습니다.";
        ArticleResponseDto response = articleService.postArticle(writer.getId(), title, content);

        // when
        articleService.deleteArticle(writer.getId(), response.getArticleId());

        // then
        assertThat(articleRepository.findById(response.getArticleId()))
                .isEmpty();
    }

    @Test
    public void 커뮤니티_글_단일_조회_작성자() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        String title = "[후기] 일본 갔다온 후기입니다.";
        String content = "재미있었습니다.";
        ArticleResponseDto response = articleService.postArticle(writer.getId(), title, content);

        // when
        OneArticleResponseDto result = articleService.oneArticle(writer.getId(), response.getArticleId());

        // then
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getNickname()).isEqualTo(writer.getNickname());
        assertThat(result.isWritten()).isTrue();
    }

    @Test
    public void 커뮤니티_글_단일_조회_작성자아닌_경우() throws Exception {
        // given
        Long memberId = 3L;
        Member writer = memberStep.createDefault();
        String title = "[후기] 일본 갔다온 후기입니다.";
        String content = "재미있었습니다.";
        ArticleResponseDto response = articleService.postArticle(writer.getId(), title, content);

        // when
        OneArticleResponseDto result = articleService.oneArticle(memberId, response.getArticleId());

        // then
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getNickname()).isEqualTo(writer.getNickname());
        assertThat(result.isWritten()).isFalse();
    }


    @Test
    public void 커뮤니티_글_여러_조회() throws Exception {
        // given
        Member writer = memberStep.createDefault();
        for (int i = 0; i < 200; i++) {
            articleService.postArticle(writer.getId(),
                    String.format("[%s] 번째 타이틀", i),
                    String.format("[%s] 번째 내용", i));
        }

        // when
        Page<ArticleResponseDto> response = articleService.allArticle(PageRequest.of(0, 10));

        // then
        assertThat(response.getContent().size()).isEqualTo(10);
        assertThat(response.getTotalPages()).isEqualTo(20);
    }


}