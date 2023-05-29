package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.article.QnaRequestDto;
import com.example.gabojago_server.dto.response.article.qna.PageQnaResponseDto;
import com.example.gabojago_server.dto.response.article.qna.QnaResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.article.QnaService;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QnaController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class QnaControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QnaService qnaService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @DisplayName("[GET] [/api/qna/posts] Qna 게시판 모두 조회")
    public void getArticleListTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        given(qnaService.allQna(pageable)).willReturn(stub(pageable));

        mockMvc.perform(get("/api/qna/posts")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getArticleList"))
                .andDo(document("article/qna/all",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                pageableDocsWithContent(createPageQnaResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[GET] [/api/qna/posts/{articleId}] Qna 게시판 단일 조회")
    public void getOneQnaTest() throws Exception {


        given(qnaService.oneQna(0L, 1L))
                .willReturn(createQnaResponseDto());

        mockMvc.perform(get("/api/qna/posts/{articleId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getOneQna"))
                .andDo(document("article/qna/one",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("articleId").description("QnA 게시글 ID")),
                        responseFields(
                                fieldWithPath("id").description("QnA 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("QnA 게시글 제목"),
                                fieldWithPath("content").description("QnA 게시글 내용"),
                                fieldWithPath("review").description("QnA 게시글 조회수"),
                                fieldWithPath("selected").description("QnA 게시글 채택 여부"),
                                fieldWithPath("written").description("QnA 게시글 작성자인지 여부")
                        )
                ));

    }

    @Test
    @DisplayName("[POST] [/api/qna/posts] Qna 게시글 등록")
    @WithMockUser(value = "1")
    public void createQnaArticleTest() throws Exception {

        QnaRequestDto request = createQnaRequestDto();
        given(qnaService.postQna(1L, request.getTitle(), request.getContent(), request.isSelected()))
                .willReturn(createQnaResponseDto());

        mockMvc.perform(post("/api/qna/post")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createQnaArticle"))
                .andDo(document("article/qna/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("QnA 제목"),
                                fieldWithPath("content").description("QnA 내용"),
                                fieldWithPath("selected").description("채택 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").description("QnA 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("QnA 게시글 제목"),
                                fieldWithPath("content").description("QnA 게시글 내용"),
                                fieldWithPath("review").description("QnA 게시글 조회수"),
                                fieldWithPath("selected").description("QnA 게시글 채택 여부"),
                                fieldWithPath("written").description("QnA 게시글 작성자인지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("[PUT] [/api/qna/{id}]  Qna 게시글 수정")
    @WithMockUser(value = "1")
    public void changeQnaArticleTest() throws Exception {


        QnaRequestDto request = createQnaRequestDto();

        given(qnaService.changeQnaArticle(1L, 1L, request.getTitle(), request.getContent(), request.isSelected()))
                .willReturn(createQnaResponseDto());

        mockMvc.perform(put("/api/qna/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("changeQnaArticle"))
                .andDo(document("article/qna/change",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("QnA 게시글 ID")),
                        requestFields(
                                fieldWithPath("title").description("QnA 제목"),
                                fieldWithPath("content").description("QnA 내용"),
                                fieldWithPath("selected").description("채택 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").description("QnA 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("QnA 게시글 제목"),
                                fieldWithPath("content").description("QnA 게시글 내용"),
                                fieldWithPath("review").description("QnA 게시글 조회수"),
                                fieldWithPath("selected").description("QnA 게시글 채택 여부"),
                                fieldWithPath("written").description("QnA 게시글 작성자인지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("[DELETE] [/api/qna/{id}]  Qna 게시글 삭제")
    @WithMockUser(value = "1")
    public void deleteQnaArticleTest() throws Exception {

        willDoNothing().given(qnaService).deleteQnaArticle(1L, 1L);

        mockMvc.perform(delete("/api/qna/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteQnaArticle"))
                .andDo(document("article/qna/delete",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("QnA 게시글 ID")),
                        responseFields(
                                fieldWithPath("status").description("성공 응답")
                        )
                ));

    }


    private Page<PageQnaResponseDto> stub(Pageable pageable) {
        PageQnaResponseDto response = PageQnaResponseDto.builder()
                .id(1L)
                .nickname("test")
                .title("테스트 제목")
                .content("테스트 내용")
                .isSelected(false)
                .review(3)
                .build();

        PageQnaResponseDto other = PageQnaResponseDto.builder()
                .id(2L)
                .nickname("test")
                .title("테스트 제목 2")
                .content("테스트 내용 2")
                .isSelected(true)
                .review(999)
                .build();
        return new PageImpl<>(List.of(response, other), pageable, 2);
    }

    private Map<String, String> createPageQnaResponseDtoResponseBody() {
        return Map.of(
                "content[].id", "동행글 ID",
                "content[].nickname", "작성자 닉네임",
                "content[].title", "동행글 제목",
                "content[].content", "동행글 내용",
                "content[].review", "동행글 조회수",
                "content[].selected", "채택 여부"
        );
    }

    private QnaResponseDto createQnaResponseDto() {
        return QnaResponseDto.builder()
                .id(1L)
                .nickname("테스트 닉네임")
                .title("테스트 제목")
                .content("테스트 내용")
                .review(1)
                .selected(true)
                .isWritten(true)
                .build();
    }

    private QnaRequestDto createQnaRequestDto() {
        return QnaRequestDto.builder()
                .title("제목")
                .content("내용")
                .selected(true)
                .build();
    }

}