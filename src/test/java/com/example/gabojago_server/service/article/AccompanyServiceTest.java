package com.example.gabojago_server.service.article;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.dto.response.article.accompany.AccompanyResponseDto;
import com.example.gabojago_server.model.article.AccompanyArticle;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.accompany.AccompanyArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import com.example.gabojago_server.service.common.EntityFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Spring Data Jpa 에 필요한 클래스를 모두 Auto 설정 , Repository 게층의 빈을 사용할 수 있다.
@Import({AccompanyService.class, JpaConfig.class, EntityFinder.class}) // 검증 대상 Import
class AccompanyServiceTest {

    @Autowired
    private AccompanyService accompanyService;

    @Autowired
    private AccompanyArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        // 테스트 수행마다 임의의 사용자를 등록
        Member member = stubMember();
        memberRepository.save(member);
    }

    @Test
    public void 동반게시글_등록테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;

        // when
        AccompanyResponseDto response =
                accompanyService.postAccompany(writer.getId(), title, content, region, startDate, endDate, recruitNumber);

        // then
        assertThat(response.getRegion()).isEqualTo(region);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getStartDate()).isEqualTo(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(response.getEndDate()).isEqualTo(endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(response.getRecruitMember()).isEqualTo(1);
        assertThat(response.getReview()).isEqualTo(0);

    }

    @Test
    public void 동반게시글_삭제테스트_작성자가_삭제하는_경우() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;
        AccompanyResponseDto articleResponse =
                accompanyService.postAccompany(writer.getId(), title, content, region, startDate, endDate, recruitNumber);

        // when
        accompanyService.deleteAccompanyArticle(writer.getId(), articleResponse.getId());

        // then

        assertThat(articleRepository.findById(articleResponse.getId()).isPresent())
                .isFalse();
    }

    @Test
    public void 동반게시글_삭제테스트_작성자가_아닌_경우() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;
        AccompanyResponseDto articleResponse =
                accompanyService.postAccompany(writer.getId(), title, content, region, startDate, endDate, recruitNumber);

        // when
        Long otherWriterId = -1L;
        try {
            accompanyService.deleteAccompanyArticle(otherWriterId, articleResponse.getId());
        } catch (Exception e) {
            // 없는 사용자 이거나 동행 게시글을 작성한 유저가 아닌 경우
        }

        // then

        assertThat(articleRepository.findById(articleResponse.getId()).isPresent())
                .isTrue();
    }

    @Test
    public void 동반게시글_수정테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;
        AccompanyResponseDto articleResponse =
                accompanyService.postAccompany(writer.getId(), title, content, region, startDate, endDate, recruitNumber);

        // when
        String newTile = "대만 동행 구해요 (수정)"; //제목만 변경
        accompanyService.changeAccompanyArticle(writer.getId(),
                articleResponse.getId(),
                newTile,
                articleResponse.getContent(),
                articleResponse.getRegion(),
                startDate,
                endDate,
                articleResponse.getRecruitMember()
        );

        // then
        Optional<AccompanyArticle> response = articleRepository.findById(articleResponse.getId());
        assertThat(response.isPresent()).isTrue();
        assertThat(response.get().getTitle()).isEqualTo(newTile);
        assertThat(response.get().getContent()).isEqualTo(content);

    }

    @Test
    public void 동반게시글_조회테스트() throws Exception {
        // given
        Member writer = memberRepository.findByEmail("test@test.com").get(); // 임의의 사용자를 가져옴
        String title = "대만 동행 구해요";
        String content = "혼자가는 첫 대만여행이에요 ...";
        String region = "타이페이";
        LocalDate startDate = LocalDate.of(2023, 5, 9);
        LocalDate endDate = LocalDate.of(2023, 5, 14);
        int recruitNumber = 1;
        AccompanyResponseDto articleResponse =
                accompanyService.postAccompany(writer.getId(), title, content, region, startDate, endDate, recruitNumber);

        // when
        AccompanyResponseDto response = accompanyService.oneAccompany(writer.getId(), articleResponse.getId());

        // then
        assertThat(response.getRegion()).isEqualTo(region);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getStartDate()).isEqualTo(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(response.getEndDate()).isEqualTo(endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(response.getRecruitMember()).isEqualTo(1);
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