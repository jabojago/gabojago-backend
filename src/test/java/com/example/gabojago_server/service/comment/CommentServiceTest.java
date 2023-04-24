package com.example.gabojago_server.service.comment;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.accompany.AccompanyArticleRepository;
import com.example.gabojago_server.repository.articlecomment.ArticleCommentRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentService.class, JpaConfig.class})
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    AccompanyArticleRepository articleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleCommentRepository articleCommentRepository;

    @BeforeEach
    void setup() {
        // 테스트 수행마다 임의의 사용자를 등록
        Member writer = stubMember();
        memberRepository.save(writer);

        // 테스트 수행마다 임의의 게시글 등록
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;
        articleRepository.save(AccompanyArticle.createAccompanyArticle(
                writer, title, content, 0, region, startDate, endDate, recruitNumber
        ));
    }

    @Test
    public void 게시글에_댓글_달기_테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        Article article = articleRepository.findAll().stream().findFirst().get(); // 등록한 게시글 가져옴
        String content = "좋아요";

        // when
        CommentResponseDto response = commentService.createComment(writer.getId(), article.getId(), content);

        // then
        assertThat(response.getCommentId()).isNotNull();
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getWriterNickname()).isEqualTo("test Nickname");
        assertThat(response.isWritten()).isTrue();

    }

    @Test
    public void 게시글에_댓글_수정_테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        Article article = articleRepository.findAll().stream().findFirst().get(); // 등록한 게시글 가져옴
        String content = "좋아요";
        CommentResponseDto comment = commentService.createComment(writer.getId(), article.getId(), content);

        // when
        String newContent = "싫어요";
        CommentResponseDto response = commentService.changeComment(writer.getId(), comment.getCommentId(), newContent);

        // then
        assertThat(response.getContent()).isEqualTo(newContent);
        assertThat(response.getWriterNickname()).isEqualTo("test Nickname");

    }

    @Test
    public void 게시글에_댓글_삭제_테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        Article article = articleRepository.findAll().stream().findFirst().get(); // 등록한 게시글 가져옴
        String content = "좋아요";
        CommentResponseDto comment = commentService.createComment(writer.getId(), article.getId(), content);

        // when
        commentService.removeComment(writer.getId(), comment.getCommentId());

        // then
        assertThat(commentService.getAllComments(article.getId()).size())
                .isEqualTo(0);

    }

    @Test
    public void 게시글_댓글_모두_조회() throws Exception {

        //given
        Article article = articleRepository.findAll().stream().findFirst().get(); // 등록한 게시글 가져옴
        SecurityContextHolder.getContext().setAuthentication(null);
        //when
        List<CommentResponseDto> allComments = commentService.getAllComments(article.getId());
        //then
        assertThat(allComments.size()).isEqualTo(0);
    }


    private Member stubMember() {
        return Member.builder()
                .name("test name")
                .email("test@test.com")
                .birth("2023-04-06")
                .nickname("test Nickname")
                .build();
    }
}