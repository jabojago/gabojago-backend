package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.article.ArticleRequestDto;
import com.example.gabojago_server.dto.response.article.community.ArticleResponseDto;
import com.example.gabojago_server.dto.response.article.community.OneArticleResponseDto;
import com.example.gabojago_server.dto.response.article.community.PageArticleResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.article.ArticleService;
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

@WebMvcTest(ArticleController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class CommunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @DisplayName("[GET] [/api/articles/posts] 커뮤니티 게시판 모두 조회")
    public void getCommunityListTest() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);

        given(articleService.allArticle(pageRequest))
                .willReturn(stubPageArticleResponseDto(pageRequest));

        mockMvc.perform(get("/api/articles/posts")
                        .param("page", String.valueOf(pageRequest.getPageNumber()))
                        .param("size", String.valueOf(pageRequest.getPageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getArticleList"))
                .andDo(document("article/community/all",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈")),
                        responseFields(
                                pageableDocsWithContent(createPageArticleResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[GET] [/api/articles/posts/{articleId} 커뮤니티 게시판 단일 조회")
    public void getOneCommunityTest() throws Exception {

        given(articleService.oneArticle(0L, 1L))
                .willReturn(createOneArticleResponseDto());

        mockMvc.perform(get("/api/articles/posts/{articleId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getCommunityArticle"))
                .andDo(document("article/community/one",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("articleId").description("커뮤니티 게시글 ID")),
                        responseFields(
                                fieldWithPath("articleId").description("커뮤니티 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("커뮤니티 게시글 제목"),
                                fieldWithPath("content").description("커뮤니티 게시글 내용"),
                                fieldWithPath("review").description("커뮤니티 게시글 조회수"),
                                fieldWithPath("written").description("커뮤니티 게시글 작성자 여부")
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/articles/post] 커뮤니티 게시글 등록")
    @WithMockUser(value = "1")
    public void createCommunityArticleTest() throws Exception {
        ArticleRequestDto request = createArticleRequestDto();
        given(articleService.postArticle(1L, request.getTitle(), request.getContent()))
                .willReturn(createArticleResponseDto());

        mockMvc.perform(post("/api/articles/post")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createCommunityArticle"))
                .andDo(document("article/community/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("커뮤니티 제목"),
                                fieldWithPath("content").description("커뮤니티 내용")
                        ),
                        responseFields(
                                fieldWithPath("articleId").description("커뮤니티 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("커뮤니티 게시글 제목"),
                                fieldWithPath("content").description("커뮤니티 게시글 내용"),
                                fieldWithPath("review").description("커뮤니티 게시글 조회수")
                        )
                ));
    }

    @Test
    @DisplayName("[PUT] [/api/articles/{id}] 커뮤니티 게시글 수정")
    @WithMockUser(value = "1")
    public void changeCommunityArticleTest() throws Exception {
        ArticleRequestDto request = createArticleRequestDto();

        given(articleService.changeArticle(1L, 1L, request.getTitle(), request.getContent()))
                .willReturn(createArticleResponseDto());

        mockMvc.perform(put("/api/articles/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("changeCommunityArticle"))
                .andDo(document("article/community/change",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("커뮤니티 게시글 ID")),
                        requestFields(
                                fieldWithPath("title").description("커뮤니티 제목"),
                                fieldWithPath("content").description("커뮤니티 내용")
                        ),
                        responseFields(
                                fieldWithPath("articleId").description("커뮤니티 게시글 ID"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("title").description("커뮤니티 게시글 제목"),
                                fieldWithPath("content").description("커뮤니티 게시글 내용"),
                                fieldWithPath("review").description("커뮤니티 게시글 조회수")
                        )
                ));
    }

    @Test
    @DisplayName("[DELETE] [/api/articles/{id}] 커뮤니티 게시글 삭제")
    @WithMockUser(value = "1")
    public void deleteCommunityArticleTest() throws Exception {
        willDoNothing().given(articleService).deleteArticle(1L, 1L);

        mockMvc.perform(delete("/api/articles/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCommunityArticle"))
                .andDo(document("article/community/delete",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(parameterWithName("id").description("커뮤니티 게시글 ID")),
                        responseFields(
                                fieldWithPath("status").description("성공 응답")
                        )
                ));
    }

    private Page<PageArticleResponseDto> stubPageArticleResponseDto(Pageable pageable) {
        PageArticleResponseDto stub = PageArticleResponseDto.builder()
                .id(1L)
                .nickname("테스트 닉네임")
                .title("제목")
                .content("내용")
                .review(99)
                .build();
        return new PageImpl<>(List.of(stub), pageable, 1);
    }


    private Map<String, String> createPageArticleResponseDtoResponseBody() {
        return Map.of(
                "content[].articleId", "커뮤니티글 ID",
                "content[].nickname", "작성자 닉네임",
                "content[].title", "커뮤니티글 제목",
                "content[].content", "커뮤니티글 내용",
                "content[].review", "커뮤니티글 조회수"
        );
    }

    private OneArticleResponseDto createOneArticleResponseDto() {
        return OneArticleResponseDto.builder()
                .articleId(1L)
                .nickname("테스트 닉네임")
                .title("테스트 제목")
                .content("테스트 내용")
                .review(1)
                .isWritten(true)
                .build();
    }

    private ArticleResponseDto createArticleResponseDto() {
        return ArticleResponseDto.builder()
                .articleId(1L)
                .nickname("테스트 닉네임")
                .title("테스트 제목")
                .content("테스트 내용")
                .review(1)
                .build();
    }

    private ArticleRequestDto createArticleRequestDto() {
        return new ArticleRequestDto("제목", "내용");
    }
}

