package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.member.ChangeNickNameRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePasswordRequestDto;
import com.example.gabojago_server.dto.request.member.ChangePhoneRequestDto;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("[GET] [/api/members/myInfo] 회원 정보 조회")
    @WithMockUser(value = "1")
    public void myInfoTest() throws Exception {
        given(memberService.getMyInfo())
                .willReturn(createMemberResponseDto());

        mockMvc.perform(get("/api/members/myInfo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getMyMemberInfo"))
                .andDo(document("member/info/memberInfo",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/members/nickname] 회원 닉네임 변경")
    @WithMockUser(value = "1")
    public void changeMemberNicknameTest() throws Exception {
        ChangeNickNameRequestDto request =  createChangeNicknameRequestDto();

        given(memberService.changeNickname(request.getEmail(), request.getNickname()))
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
                                fieldWithPath("email").description("회원 이메일"),
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

        given(memberService.changePassword(request.getEmail(), request.getExPassword(), request.getNewPassword()))
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
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("exPassword").description("현재 비밀번호"),
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

        given(memberService.changePhone(request.getEmail(), request.getPhone()))
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
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("phone").description("새로운 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ));
    }

    private MemberResponseDto createMemberResponseDto(){
        return MemberResponseDto.builder()
                .email("test@test.com")
                .nickname("test")
                .build();
    }

    private ChangeNickNameRequestDto createChangeNicknameRequestDto(){
        return new ChangeNickNameRequestDto(
                "test@test.com", "test"
        );
    }

    private ChangePasswordRequestDto createChangePasswordRequestDto(){
        return new ChangePasswordRequestDto(
                "test@test.com", "test123!@#", "test1234!@#"
        );
    }

    private ChangePhoneRequestDto createChangePhoneRequestDto() {
        return new ChangePhoneRequestDto(
                "test@test.com", "010-0000-0000"
        );
    }
}
