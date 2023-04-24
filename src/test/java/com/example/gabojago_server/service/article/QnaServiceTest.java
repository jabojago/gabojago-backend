package com.example.gabojago_server.service.article;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.dto.response.article.QnaResponseDto;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.Qna.QnaArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QnaService.class, JpaConfig.class})
class QnaServiceTest {

    @Autowired
    private QnaService qnaService;

    @Autowired
    private QnaArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Member member = stubMember();
        memberRepository.save(member);
    }

    @Test
    public void QnA게시글_등록테스트() throws Exception {
        //given
        Member writer = memberRepository.findByEmail("test@test.com").get();
        String title = "마카오 맛집 알려주세요";
        String content = "마카오 여행 처음인데 맛집 알려주세요!";

        //when
        QnaResponseDto response =
                qnaService.postQna(writer.getId(), title, content);

        //then
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getReview()).isEqualTo(0);
        assertThat(response.isSelected()).isEqualTo(false);
    }

    @Test
    public void QnA게시글_삭제테스트_작성자가_삭제하는경우() throws Exception {
        //given
        Member writer = memberRepository.findByEmail("test@test.com").get();
        String title = "마카오 맛집 알려주세요";
        String content = "마카오 여행 처음인데 맛집 알려주세요!";
        QnaResponseDto response =
                qnaService.postQna(writer.getId(), title, content);

        //when
        qnaService.deleteQnaArticle(writer.getId(), response.getId());

        //then
        assertThat(articleRepository.findById(response.getId()).isPresent())
                .isFalse();

    }

    @Test
    public void Qna게시글_삭제테스트_작성자가_아닌데_삭제하려는경우() throws Exception {
        //given
        Member writer = memberRepository.findByEmail("test@test.com").get();
        String title = "마카오 맛집 알려주세요";
        String content = "마카오 여행 처음인데 맛집 알려주세요!";
        QnaResponseDto response =
                qnaService.postQna(writer.getId(), title, content);

        //when
        Long otherWriterId = -1L;
        try {
            qnaService.deleteQnaArticle(otherWriterId, response.getId());
        } catch (Exception e) {

        }

        //then
        assertThat(articleRepository.findById(response.getId()).isPresent())
                .isTrue();
    }

    @Test
    public void Qna게시글_수정테스트() throws Exception {
        //given
        Member writer = memberRepository.findByEmail("test@test.com").get();
        String title = "마카오 맛집 알려주세요";
        String content = "마카오 여행 처음인데 맛집 알려주세요!";
        QnaResponseDto articleResponse =
                qnaService.postQna(writer.getId(), title, content);

        //when
        String newTitle = "마카오 맛집 알려주세요 (수정)";
        QnaResponseDto response = qnaService.changeQnaArticle(writer.getId(),
                articleResponse.getId(),
                newTitle,
                articleResponse.getContent(),
                true
        );

        //then
        assertThat(articleRepository.findById(articleResponse.getId()).isPresent());
        assertThat(articleRepository.findById(response.getId()))
                .isEqualTo(articleRepository.findById(articleResponse.getId()));
        assertThat(response.getTitle()).isEqualTo(newTitle);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.isSelected()).isEqualTo(true);

    }

    @Test
    public void Qna게시글_조회테스트() throws Exception {
        //given
        Member writer = memberRepository.findByEmail("test@test.com").get();
        String title = "마카오 맛집 알려주세요";
        String content = "마카오 여행 처음인데 맛집 알려주세요!";
        QnaResponseDto articleResponse =
                qnaService.postQna(writer.getId(), title, content);

        //when
        QnaResponseDto response = qnaService.oneQna(writer.getId(), articleResponse.getId());

        //then
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.isSelected()).isEqualTo(false);
        assertThat(response.getReview()).isEqualTo(1);
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
