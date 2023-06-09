package com.example.gabojago_server.web.controller.comment;

import com.example.gabojago_server.dto.request.comment.CommentRequestDto;
import com.example.gabojago_server.dto.response.NormalResponse;
import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gabojago_server.config.SecurityUtil.getCurrentMemberIdx;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable(value = "postId") Long postId) {
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> postComment(@PathVariable(value = "postId") Long postId, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(getCurrentMemberIdx(), postId, request.getContent()));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> changeComment(@PathVariable(value = "commentId") Long commentId, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.changeComment(getCurrentMemberIdx(), commentId, request.getContent()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<NormalResponse> deleteComment(@PathVariable(value = "commentId") Long commentId) {
        commentService.removeComment(getCurrentMemberIdx(), commentId);
        return ResponseEntity.ok(NormalResponse.success());
    }

}
