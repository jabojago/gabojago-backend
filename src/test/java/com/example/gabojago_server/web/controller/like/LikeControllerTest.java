package com.example.gabojago_server.web.controller.like;

import com.example.gabojago_server.config.TestSecurityConfig;
import com.example.gabojago_server.dto.response.like.LikeResponseDto;
import com.example.gabojago_server.jwt.JwtTokenProvider;
import com.example.gabojago_server.service.like.LikeService;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@AutoConfigureRestDocs(uriHost = "gabojago.shop", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(TestSecurityConfig.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @DisplayName("[POST] [/api/like/{articleId}] 좋아요 기능")
    @WithMockUser(value = "1")
    public void likeTest() throws Exception {

        given(likeService.clickLike(1L, 1L)).willReturn(mock(LikeResponseDto.class));

        mockMvc.perform(post("/api/like/{articleId}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ${AccessToken}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("like"))
                .andDo(document("like/like",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("성공 응답")
                        )
                ));
    }

    @Test
    @DisplayName("[GET] [/api/like/{articleId}] 좋아요 기능")
    public void getLikeNumberTest() throws Exception {

        given(likeService.getLike(1L)).willReturn(4);

        mockMvc.perform(get("/api/like/{articleId}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getLikeNumber"))
                .andDo(document("like/getLikeNumber",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("likeNumber").description("좋아요 수")
                        )
                ));
    }

}