package com.example.gabojago_server.web.controller.article;

import com.example.gabojago_server.dto.request.article.QnaRequestDto;
import com.example.gabojago_server.dto.response.NormalResponse;
import com.example.gabojago_server.dto.response.article.qna.PageQnaResponseDto;
import com.example.gabojago_server.dto.response.article.qna.QnaResponseDto;
import com.example.gabojago_server.service.article.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.gabojago_server.config.SecurityUtil.getCurrentMemberIdx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {
    private final QnaService qnaService;

    @GetMapping("/posts")
    public ResponseEntity<Page<PageQnaResponseDto>> getArticleList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(qnaService.allQna(pageable));
    }

    @GetMapping("/posts/{articleId}")
    public ResponseEntity<QnaResponseDto> getOneQna(@PathVariable(value = "articleId") Long articleId) {
        return ResponseEntity.ok(qnaService.oneQna(getCurrentMemberIdx(), articleId));
    }

    @PostMapping("/post")
    public ResponseEntity<QnaResponseDto> createQnaArticle(@RequestBody QnaRequestDto requestDto) {
        return ResponseEntity.ok(qnaService.postQna(getCurrentMemberIdx(), requestDto.getTitle(), requestDto.getContent(), requestDto.isSelected()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QnaResponseDto> changeQnaArticle(@PathVariable(value = "id") Long articleId, @RequestBody QnaRequestDto requestDto) {
        return ResponseEntity.ok(qnaService.changeQnaArticle(getCurrentMemberIdx(), articleId, requestDto.getTitle(), requestDto.getContent(), requestDto.isSelected()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NormalResponse> deleteQnaArticle(@PathVariable(value = "id") Long articleId) {
        qnaService.deleteQnaArticle(getCurrentMemberIdx(), articleId);
        return ResponseEntity.ok(NormalResponse.success());
    }

}
