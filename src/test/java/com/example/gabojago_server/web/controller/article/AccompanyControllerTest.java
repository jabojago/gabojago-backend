package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.article.AccompanyRequestDto;
import com.example.gabojago_server.dto.response.article.accompany.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.accompany.PageAccompanyResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.article.AccompanyService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.example.gabojago_server.web.controller.restDocs.RestDocsUtils.onlyContent;
import static com.example.gabojago_server.web.controller.restDocs.RestDocsUtils.pageableDocsWithContent;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccompanyController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class AccompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccompanyService accompanyService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("[GET] [/api/accompany/posts] 게시글들 요청 테스트")
    public void getArticleListTest() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);

        given(accompanyService.allAccompany(pageRequest)).willReturn(stubPageAccompanyResponseDto(pageRequest));

        mockMvc.perform(get("/api/accompany/posts")
                        .param("page", String.valueOf(pageRequest.getPageNumber()))
                        .param("size", String.valueOf(pageRequest.getPageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getArticleList"))
                .andDo(document("article/accompany/all",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                pageableDocsWithContent(createPageAccompanyResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[GET] [/api/accompany/posts/{articleId}] 단일 게시글 요청 테스트")
    public void getOneAccompanyTest() throws Exception {

        given(accompanyService.oneAccompany(0L, 1L)).willReturn(stubAccompanyResponseDto());

        mockMvc.perform(get("/api/accompany/posts/{articleId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getOneAccompany"))
                .andDo(document("article/accompany/one",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("동행 게시글 ID")
                        ),
                        responseFields(
                                onlyContent(createAccompanyResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/accompany/posts] 동행 게시글 등록")
    @WithMockUser(value = "1")
    public void createAccompanyArticleTest() throws Exception {
        AccompanyRequestDto request = stubAccompanyRequestDto();

        given(accompanyService.postAccompany(1L, request.getTitle(), request.getContent(),
                request.getRegion(), request.getStartDate(), request.getEndDate(), request.getRecruitMember()
        )).willReturn(stubAccompanyResponseDto());

        mockMvc.perform(post("/api/accompany/post")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createAccompanyArticle"))
                .andDo(document("article/accompany/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                onlyContent(createAccompanyRequestDtoRequestBody())
                        ),
                        responseFields(
                                onlyContent(createAccompanyResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[PUT] [/api/accompany/{id}] 동행 게시글 등록")
    @WithMockUser(value = "1")
    public void changeAccompanyArticleTest() throws Exception {
        AccompanyRequestDto request = stubAccompanyRequestDto();

        given(accompanyService.changeAccompanyArticle(1L, 1L, request.getTitle(), request.getContent(),
                request.getRegion(), request.getStartDate(), request.getEndDate(), request.getRecruitMember()
        )).willReturn(stubAccompanyResponseDto());

        mockMvc.perform(put("/api/accompany/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("changeAccompanyArticle"))
                .andDo(document("article/accompany/change",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("동행 게시글 ID")),
                        requestFields(
                                onlyContent(createAccompanyRequestDtoRequestBody())
                        ),
                        responseFields(
                                onlyContent(createAccompanyResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[DELETE] [/api/accompany/{id}] 동행 게시글 등록")
    @WithMockUser(value = "1")
    public void deleteAccompanyArticle() throws Exception {

        willDoNothing().given(accompanyService).deleteAccompanyArticle(1L, 1L);

        mockMvc.perform(delete("/api/accompany/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteAccompanyArticle"))
                .andDo(document("article/accompany/delete",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("동행 게시글 ID")),
                        responseFields(
                                fieldWithPath("status").description("성공 응답")
                        )
                ));
    }

    private Map<String, String> createPageAccompanyResponseDtoResponseBody() {
        return Map.of(
                "content[].id", "동행글 ID",
                "content[].nickname", "작성자 닉네임",
                "content[].title", "동행글 제목",
                "content[].content", "동행글 내용",
                "content[].review", "동행글 조회수",
                "content[].region", "동행 지역",
                "content[].startDate", "동행 시작 날짜",
                "content[].endDate", "동행글 마지막 날짜",
                "content[].recruitMember", "모집 인원"
        );
    }


    private Page<PageAccompanyResponseDto> stubPageAccompanyResponseDto(Pageable pageable) {
        PageAccompanyResponseDto stub = PageAccompanyResponseDto.builder()
                .id(1L)
                .nickname("테스트 닉네임")
                .title("제목")
                .content("내용")
                .review(99)
                .region("서울")
                .startDate("2999-12-31")
                .endDate("3999-12-31")
                .recruitMember(2)
                .build();
        return new PageImpl<>(List.of(stub), pageable, 1);
    }

    private Map<String, String> createAccompanyResponseDtoResponseBody() {
        return Map.of(
                "id", "동행글 ID",
                "nickname", "작성자 닉네임",
                "title", "동행글 제목",
                "content", "동행글 내용",
                "review", "동행글 조회수",
                "region", "동행 지역",
                "startDate", "동행 시작 날짜",
                "endDate", "동행글 마지막 날짜",
                "recruitMember", "모집 인원",
                "written", "작성자인지 여부"
        );
    }

    private AccompanyResponseDto stubAccompanyResponseDto() {
        return AccompanyResponseDto.builder()
                .id(1L)
                .nickname("테스트 닉네임")
                .title("제목")
                .content("내용")
                .review(99)
                .region("서울")
                .startDate("2999-12-31")
                .endDate("3999-12-31")
                .recruitMember(2)
                .build();

    }


    private AccompanyRequestDto stubAccompanyRequestDto() {
        return AccompanyRequestDto.builder()
                .title("제목")
                .content("내용")
                .region("서울")
                .startDate(LocalDate.of(2099, 12, 31))
                .endDate(LocalDate.of(3099, 12, 31))
                .recruitMember(2)
                .build();
    }

    private Map<String, String> createAccompanyRequestDtoRequestBody() {
        return Map.of(
                "title", "동행글 제목",
                "content", "동행글 내용",
                "region", "동행 지역",
                "startDate", "동행 시작 날짜",
                "endDate", "동행글 마지막 날짜",
                "recruitMember", "모집 인원"
        );
    }

}