package com.example.gabojago_server.service.common;

import com.example.gabojago_server.config.JpaConfig;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.member.MemberRepository;
import com.example.gabojago_server.steps.MemberStep;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({JpaConfig.class, EntityFinder.class})
class EntityFinderTest {

    @Autowired
    private EntityFinder entityFinder;

    @Autowired
    private MemberRepository memberRepository;

    private MemberStep memberStep;

    @BeforeEach
    void setup() {
        memberStep = new MemberStep(memberRepository);
    }

    @Test
    @DisplayName("ID 로 Entity 를 찾아온다.")
    public void findXXX() throws Exception {
        // given
        Member baseMember = memberStep.createDefault();

        // when
        Member member = entityFinder.findMember(baseMember.getId());

        // then
        Assertions.assertThat(member.getId()).isEqualTo(baseMember.getId());
    }

    @Test
    @DisplayName("ID 로 Entity 를 찾아오지 못하는 경우 NOT_FOUND 에러를 던진다.")
    public void given_when_then() throws Exception {
        // given
        Long nonExistId = 99L;

        // expected
        GabojagoException gabojagoException = assertThrows(GabojagoException.class, () -> entityFinder.findMember(nonExistId));
        assertEquals(HttpStatus.NOT_FOUND.value(), gabojagoException.getStatus());

    }


}