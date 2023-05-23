package com.example.gabojago_server.web.controller.member;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.member.LoginRequestDto;
import com.example.gabojago_server.dto.request.member.MemberRequestDto;
import com.example.gabojago_server.dto.response.member.MemberResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.mail.ChangePwEmailService;
import com.example.gabojago_server.service.mail.SignupEmailService;
import com.example.gabojago_server.service.member.AuthService;
import com.example.gabojago_server.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.example.gabojago_server.web.controller.restDocs.RestDocsUtils.onlyContent;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private SignupEmailService emailService;

    @MockBean
    private ChangePwEmailService pwEmailService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("[POST] [/auth/signup] 회원가입 테스트")
    public void signupTest() throws Exception {
        MemberRequestDto request = createMemberRequestDto();

        given(authService.joinMember(request)).willReturn(stubMemberResponseDto());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("signup"))
                .andDo(document("member/auth/signup",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                onlyContent(createMemberRequestDtoRequestBody())
                        ),
                        responseFields(
                                onlyContent(createMemberResponseDtoResponseBody())
                        )
                ));

    }

    @Test
    @DisplayName("[POST] [/auth/signup/mailConfirm] 회원가입 시 이메일 인증 테스트")
    public void mailConfirmTest() throws Exception {
        String email = "test@test.com";
        String ePw = emailService.createKey();

        mockMvc.perform(post("/auth/signup/mailConfirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(email)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("mailConfirm"))
                .andDo(document("member/auth/signup/mailConfirm",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("회원 이메일")
                        ),
                        responseFields(
                                fieldWithPath("data").description("인증코드")
                        )
                ));

    }

    @Test
    @DisplayName("[POST] [/auth/login/findPw] 로그인 시 비밀번호 찾기 테스트")
    public void findPwTest() throws Exception {
        String email = "test@test.com";
        String ePw = pwEmailService.createKey();

        doNothing().when(authService).changeTempPw(email, ePw);

        mockMvc.perform(post("/auth/findPw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(email)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("findPw"))
                .andDo(document("member/auth/findPw",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath(email).description("회원 이메일")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태")
                        )
                ));

    }

    // TODO : 로그인은 따로 문서화

    private Map<String, String> createLoginRequestDtoRequestBody() {
        return Map.of(
                "email", "이메일",
                "password", "패스워드"
        );
    }

    private Map<String, String> createTokenDtoResponseBody() {
        return Map.of(
                "token", "엑세스 토큰"
        );
    }

    private LoginRequestDto createLoginRequestDto() {
        return LoginRequestDto.builder()
                .email("test@test.com")
                .password("test123!@#")
                .build();
    }

    private Map<String, String> createMemberResponseDtoResponseBody() {
        return Map.of(
                "email", "이메일",
                "nickname", "닉네임"
        );
    }

    private Map<String, String> createMemberRequestDtoRequestBody() {
        return Map.of(
                "email", "이메일",
                "password", "패스워드",
                "name", "이름",
                "nickname", "닉네임",
                "birth", "생일",
                "phone", "핸드폰 번호"
        );
    }

    private MemberRequestDto createMemberRequestDto() {
        return MemberRequestDto.builder()
                .email("test@test.com")
                .password("test123123!@#")
                .name("test")
                .nickname("test")
                .birth("2021-12-31")
                .phone("010-0000-0000")
                .build();
    }

    private MemberResponseDto stubMemberResponseDto() {
        return MemberResponseDto.builder()
                .email("test@test.com")
                .nickname("test")
                .build();
    }


}
