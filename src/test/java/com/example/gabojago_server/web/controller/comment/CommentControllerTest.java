package com.example.gabojago_server.web.controller.comment;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.request.comment.CommentRequestDto;
import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.comment.CommentService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static com.example.gabojago_server.web.controller.restDocs.RestDocsUtils.onlyContent;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("[GET] [/api/comment/{postId}] 게시글 모든 댓글 조회 테스트")
    public void getAllCommentsTest() throws Exception {

        given(commentService.getAllComments(1L)).willReturn(stubCommentResponses());

        mockMvc.perform(get("/api/comment/{postId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllComments"))
                .andDo(document("comment/all",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                onlyContent(createCommentResponseDtosResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[POST] [/api/comment/{postId}] 게시글에 댓글 등록")
    @WithMockUser(value = "1")
    public void postCommentTest() throws Exception {
        String content = "테스트 댓글";
        CommentRequestDto request = createCommentRequestDto(content);

        given(commentService.createComment(1L, 1L, content))
                .willReturn(stubCommentResponseDto());

        mockMvc.perform(post("/api/comment/{postId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("postComment"))
                .andDo(document("comment/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                onlyContent(createCommentRequestDtoRequestBody())
                        ),
                        responseFields(
                                onlyContent(createCommentResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[PUT] [/api/comment/{commentId}] 게시글에 댓글 수정")
    @WithMockUser(value = "1")
    public void changeCommentTest() throws Exception {

        String updateContent = "테스트 수정 댓글";
        CommentRequestDto request = createCommentRequestDto(updateContent);

        given(commentService.changeComment(1L, 1L, updateContent))
                .willReturn(stubCommentResponseDto());

        mockMvc.perform(put("/api/comment/{commentId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("changeComment"))
                .andDo(document("comment/change",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                onlyContent(createCommentRequestDtoRequestBody())
                        ),
                        responseFields(
                                onlyContent(createCommentResponseDtoResponseBody())
                        )
                ));
    }

    @Test
    @DisplayName("[DELETE] [/api/comment/{commentId}] 게시글에 댓글 삭제")
    @WithMockUser(value = "1")
    public void deleteCommentTest() throws Exception {

        willDoNothing().given(commentService).removeComment(1L, 1L);

        mockMvc.perform(delete("/api/comment/{commentId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteComment"))
                .andDo(document("comment/delete",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("성공 응답")
                        )
                ));
    }

    private Map<String, String> createCommentRequestDtoRequestBody() {
        return Map.of(
                "content", "댓글 내용"
        );
    }

    private Map<String, String> createCommentResponseDtoResponseBody() {
        return Map.of(
                "commentId", "댓글 ID",
                "writerNickname", "작성자 닉네임",
                "content", "댓글 내용",
                "written", "작성자인지 여부"
        );
    }

    private CommentRequestDto createCommentRequestDto(String content) {
        return CommentRequestDto.builder()
                .content(content)
                .build();
    }

    private Map<String, String> createCommentResponseDtosResponseBody() {
        return Map.of(
                "[].commentId", "댓글 ID",
                "[].writerNickname", "작성자 닉네임",
                "[].content", "댓글 내용",
                "[].written", "작성자인지 여부"
        );
    }

    private List<CommentResponseDto> stubCommentResponses() {
        return List.of(stubCommentResponseDto());
    }

    private CommentResponseDto stubCommentResponseDto() {
        return CommentResponseDto.builder()
                .commentId(1L)
                .content("댓글")
                .writerNickname("테스트 닉네임")
                .isWritten(true)
                .build();
    }

}