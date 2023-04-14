package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.dto.request.article.AccompanyRequestDto;
import com.example.gabojago_server.dto.response.article.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.PageAccompanyResponseDto;
import com.example.gabojago_server.service.article.AccompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accompany")
public class AccompanyController {
    private final AccompanyService accompanyService;

    @GetMapping("/posts")
    public ResponseEntity<List<PageAccompanyResponseDto>> getArticleList() {
        return ResponseEntity.ok(accompanyService.allAccompany());
    }

    @GetMapping("/page/{pageNum}")
    public ResponseEntity<Page<PageAccompanyResponseDto>> pageAccompany(@PathVariable(value = "pageNum") int pageNum) {
        return ResponseEntity.ok(accompanyService.pageAccompany(pageNum));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<AccompanyResponseDto> getOneAccompany(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(accompanyService.oneAccompany(id));
    }

    @PostMapping("/post")
    public ResponseEntity<AccompanyResponseDto> createAccompanyArticle(@RequestBody AccompanyRequestDto requestDto) {
        return ResponseEntity.ok(accompanyService.postAccompany(requestDto.getTitle(),
                requestDto.getContent(), requestDto.getRegion(), requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getRecruitNumber()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccompanyResponseDto> changeAccompanyArticle(@PathVariable(value = "id") Long id, @RequestBody AccompanyRequestDto requestDto) {
        return ResponseEntity.ok(accompanyService.changeAccompanyArticle(
                id, requestDto.getTitle(), requestDto.getContent(), requestDto.getRegion(),
                requestDto.getStartDate(), requestDto.getEndDate(), requestDto.getRecruitNumber()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccompanyArticle(@PathVariable(value = "id") Long id) {
        accompanyService.deleteAccompanyArticle(id);
        return ResponseEntity.ok(new String("Success".getBytes(), StandardCharsets.UTF_8));
    }
}
