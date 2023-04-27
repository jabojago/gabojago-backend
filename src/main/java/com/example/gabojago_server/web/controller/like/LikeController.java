package com.example.gabojago_server.web.controller.like;

import com.example.gabojago_server.config.SecurityUtil;
import com.example.gabojago_server.dto.response.like.LikeClickResponse;
import com.example.gabojago_server.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{articleId}")
    public ResponseEntity<String> like(@PathVariable Long articleId) {
        likeService.clickLike(articleId, SecurityUtil.getCurrentMemberIdx());
        return ResponseEntity.ok(new String("Success".getBytes(), StandardCharsets.UTF_8));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<LikeClickResponse> getLikeNumber(@PathVariable Long articleId) {
        int likeNumber = likeService.getLike(articleId);
        return ResponseEntity.ok(new LikeClickResponse(likeNumber));
    }
}
