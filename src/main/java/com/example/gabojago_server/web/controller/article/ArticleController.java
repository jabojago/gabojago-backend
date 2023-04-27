package com.example.gabojago_server.web.controller.article;


import com.example.gabojago_server.dto.request.article.AccompanyRequestDto;
import com.example.gabojago_server.dto.request.article.ArticleRequestDto;
import com.example.gabojago_server.dto.response.article.ArticleResponseDto;
import com.example.gabojago_server.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

import static com.example.gabojago_server.config.SecurityUtil.getCurrentMemberIdx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/posts")
    public ResponseEntity<Page<ArticleResponseDto>> getArticleList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(articleService.allArticle(pageable));
    }

    @GetMapping("/posts/{articleId}")
    public ResponseEntity<ArticleResponseDto> getOneAccompany(@PathVariable(value = "articleId") Long articleId) {
        return ResponseEntity.ok(articleService.oneArticle(getCurrentMemberIdx(), articleId));
    }

    @PostMapping("/post")
    public ResponseEntity<ArticleResponseDto> createAccompanyArticle(@RequestBody ArticleRequestDto requestDto) {
        return ResponseEntity.ok(articleService.postArticle(getCurrentMemberIdx(), requestDto.getTitle(), requestDto.getContent()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDto> changeAccompanyArticle(@PathVariable(value = "id") Long articleId, @RequestBody AccompanyRequestDto requestDto) {
        return ResponseEntity.ok(articleService.changeArticle(getCurrentMemberIdx(),
                articleId, requestDto.getTitle(), requestDto.getContent()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccompanyArticle(@PathVariable(value = "id") Long articleId) {
        articleService.deleteArticle(getCurrentMemberIdx(), articleId);
        return ResponseEntity.ok(new String("Success".getBytes(), StandardCharsets.UTF_8));
    }


}
