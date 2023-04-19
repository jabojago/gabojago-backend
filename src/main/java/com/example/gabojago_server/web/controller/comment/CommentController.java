package com.example.gabojago_server.web.controller.comment;

import com.example.gabojago_server.config.SecurityUtil;
import com.example.gabojago_server.dto.request.comment.CommentRequestDto;
import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable(value = "postId") Long postId) {
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> postComment(@PathVariable(value = "postId") Long postId, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(findMemberId(), postId, request.getContent()));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> changeComment(@PathVariable(value = "commentId") Long commentId, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.changeComment(findMemberId(), commentId, request.getContent()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "commentId") Long commentId) {
        commentService.removeComment(findMemberId(), commentId);
        return ResponseEntity.ok(new String("Success".getBytes(), StandardCharsets.UTF_8));
    }

    private Long findMemberId() {
        return SecurityUtil.getCurrentMemberIdx();
    }
}
