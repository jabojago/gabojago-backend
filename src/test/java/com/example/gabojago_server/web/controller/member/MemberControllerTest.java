package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.member.ChangeNickNameRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePasswordRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePhoneRequestDto;
import com.example.gabojago_server.dto.response.member.AlarmResponseDto;
import com.example.gabojago_server.dto.response.member.MemberInfoResponseDto;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.model.alarm.AlarmEntity;
import com.example.gabojago_server.model.alarm.AlarmType;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.service.alarm.AlarmService;
import com.example.gabojago_server.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static com.example.gabojago_server.web.controller.restDocs.RestDocsUtils.pageableDocsWithContent;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AlarmService alarmService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("[GET] [/api/members/myInfo] 회원 정보 조회")
    @WithMockUser(value = "1")
    public void myInfoTest() throws Exception {
        given(memberService.getMyInfo())
                .willReturn(createMemberInfoResponseDto());

        mockMvc.perform(get("/api/members/myInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getMyMemberInfo"))
                .andDo(document("member/info/memberInfo",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("birth").description("회원 생년월일"),
                                fieldWithPath("phone").description("회원 핸드폰 번호")
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/members/nickname] 회원 닉네임 변경")
    @WithMockUser(value = "1")
    public void changeMemberNicknameTest() throws Exception {
        ChangeNickNameRequestDto request = createChangeNicknameRequestDto();

        given(memberService.changeNickname(1L, request.getNickname()))
                .willReturn(createMemberResponseDto());

        mockMvc.perform(post("/api/members/nickname")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("setMemberNickname"))
                .andDo(document("member/info/nickname",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/members/password] 회원 비밀번호 변경")
    @WithMockUser(value = "1")
    public void changeMemberPasswordTest() throws Exception {
        ChangePasswordRequestDto request = createChangePasswordRequestDto();

        given(memberService.changePassword(1L, request.getNewPassword()))
                .willReturn(createMemberResponseDto());

        mockMvc.perform(post("/api/members/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("setMemberPassword"))
                .andDo(document("member/info/password",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("newPassword").description("새로운 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/members/phone] 회원 전화번호 변경")
    @WithMockUser(value = "1")
    public void changeMemberPhoneTest() throws Exception {
        ChangePhoneRequestDto request = createChangePhoneRequestDto();

        given(memberService.changePhone(1L, request.getPhone()))
                .willReturn(createMemberResponseDto());

        mockMvc.perform(post("/api/members/phone")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("setMemberPhone"))
                .andDo(document("member/info/phone",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("phone").description("새로운 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("[GET] [/api/member/alarms] 사용자 알람 요청")
    @WithMockUser(value = "1")
    public void getAlarmListTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        given(alarmService.findAlarms(1L, pageable))
                .willReturn(createSubAlarmResponseDto(pageable));

        mockMvc.perform(get("/api/members/alarms")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAlarmList"))
                .andDo(document("member/info/alarm",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                pageableDocsWithContent(createPageAlarmResponseDtoResponseBody())
                        )
                ));
    }

    private Map<String, String> createPageAlarmResponseDtoResponseBody() {
        return Map.of(
                "content[].message", "메시지",
                "content[].alarmType", "알람 타입 COMMENT , LIKE",
                "content[].publisherNickname", "댓글 혹은 좋아요를 누른 사용자",
                "content[].nickname", "게시글 주인 닉네임",
                "content[].postId", "알람이 발생한 게시글 ID"
        );
    }

    private Page<AlarmResponseDto> createSubAlarmResponseDto(Pageable pageable) {
        AlarmResponseDto rseponse = AlarmResponseDto.from(createSubAlarmEntity(), createStubArticle(), createSubMember());
        return new PageImpl<>(List.of(rseponse), pageable, 1);
    }

    private Article createStubArticle() {
        return Article.builder()
                .writer(createSubMember())
                .title("stub Title")
                .content("stub Content")
                .build();
    }

    private AlarmEntity createSubAlarmEntity() {
        return AlarmEntity.builder()
                .alarmType(AlarmType.COMMENT)
                .member(createSubMember())
                .targetId(2L)
                .publisherId(2L)
                .build();
    }

    private Member createSubMember() {
        return Member.builder()
                .birth("2023-05-11")
                .password("test@test!@#213")
                .email("stub@gmail.com")
                .nickname("stub")
                .name("stub")
                .build();
    }


    private MemberResponseDto createMemberResponseDto() {
        return MemberResponseDto.builder()
                .email("test@test.com")
                .nickname("test")
                .build();
    }

    private MemberInfoResponseDto createMemberInfoResponseDto() {
        return MemberInfoResponseDto.builder()
                .email("test@test.com")
                .nickname("test")
                .birth("2021-12-31")
                .phone("010-0000-0000")
                .build();
    }

    private ChangeNickNameRequestDto createChangeNicknameRequestDto() {
        return new ChangeNickNameRequestDto(
                "test"
        );
    }

    private ChangePasswordRequestDto createChangePasswordRequestDto() {
        return new ChangePasswordRequestDto(
                "Test123!"
        );
    }

    private ChangePhoneRequestDto createChangePhoneRequestDto() {
        return new ChangePhoneRequestDto(
                "010-0000-0000"
        );
    }
}
