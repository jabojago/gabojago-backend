package com.example.gabojago_server.repository.article;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.member.Member;
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
class AccompanyArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("AccompanyArticle Entity 테스트")
    public void AccompanyEntitySaveTest() throws Exception {
        // given
        Member member = stubMember();
        memberRepository.save(member);

        AccompanyArticle accompanyArticle = AccompanyArticle.builder()
                .title("test title")
                .content("test content")
                .startDate(LocalDate.of(2023, 5, 9))
                .endDate(LocalDate.of(2023, 5, 12))
                .recruitNumber(4)
                .region("서울")
                .writer(member)
                .build();
        // when

        articleRepository.save(accompanyArticle);

        // then

        Assertions.assertThat(accompanyArticle.getId()).isNotNull();

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