package com.example.gabojago_server.repository.articlecomment;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.articlecomment.ArticleComment;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.ArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

@DataJpaTest
@Import(JpaConfig.class)
class ArticleCommentRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Test
    @DisplayName("articleComment Save Test")
    public void articleCommentEntitySaveTest() throws Exception {
        // given
        Member writer = stubMember();
        memberRepository.save(writer);
        AccompanyArticle accompanyArticle = stubAccompany(writer);
        articleRepository.save(accompanyArticle);

        ArticleComment articleComment = ArticleComment.builder()
                .article(accompanyArticle)
                .content("test content")
                .writer(writer)
                .build();

        // when
        articleCommentRepository.save(articleComment);

        // then
        Assertions.assertThat(articleComment.getId()).isNotNull();
    }


    private AccompanyArticle stubAccompany(Member writer) {
        return AccompanyArticle.builder()
                .title("test title")
                .content("test content")
                .startDate(LocalDate.of(2023, 5, 9))
                .endDate(LocalDate.of(2023, 5, 12))
                .recruitNumber(4)
                .region("서울")
                .writer(writer)
                .build();
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