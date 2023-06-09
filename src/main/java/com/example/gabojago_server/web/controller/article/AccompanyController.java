package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.dto.request.article.AccompanyRequestDto;
import com.example.gabojago_server.dto.response.NormalResponse;
import com.example.gabojago_server.dto.response.article.accompany.AccompanyResponseDto;
import com.example.gabojago_server.dto.response.article.accompany.PageAccompanyResponseDto;
import com.example.gabojago_server.service.article.AccompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.gabojago_server.config.SecurityUtil.getCurrentMemberIdx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accompany")
public class AccompanyController {
    private final AccompanyService accompanyService;

    @GetMapping("/posts")
    public ResponseEntity<Page<PageAccompanyResponseDto>> getArticleList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(accompanyService.allAccompany(pageable));
    }

    @GetMapping("/posts/{articleId}")
    public ResponseEntity<AccompanyResponseDto> getOneAccompany(@PathVariable(value = "articleId") Long articleId) {
        return ResponseEntity.ok(accompanyService.oneAccompany(getCurrentMemberIdx(), articleId));
    }

    @PostMapping("/post")
    public ResponseEntity<AccompanyResponseDto> createAccompanyArticle(@RequestBody AccompanyRequestDto requestDto) {
        return ResponseEntity.ok(accompanyService.postAccompany(getCurrentMemberIdx(), requestDto.getTitle(),
                requestDto.getContent(), requestDto.getRegion(), requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getRecruitMember()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccompanyResponseDto> changeAccompanyArticle(@PathVariable(value = "id") Long articleId, @RequestBody AccompanyRequestDto requestDto) {
        return ResponseEntity.ok(accompanyService.changeAccompanyArticle(getCurrentMemberIdx(),
                articleId, requestDto.getTitle(), requestDto.getContent(), requestDto.getRegion(),
                requestDto.getStartDate(), requestDto.getEndDate(), requestDto.getRecruitMember()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NormalResponse> deleteAccompanyArticle(@PathVariable(value = "id") Long articleId) {
        accompanyService.deleteAccompanyArticle(getCurrentMemberIdx(), articleId);
        return ResponseEntity.ok(NormalResponse.success());
    }
}
