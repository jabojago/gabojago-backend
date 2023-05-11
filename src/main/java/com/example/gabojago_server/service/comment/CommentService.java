package com.example.gabojago_server.service.comment;

import com.example.gabojago_server.aop.Alarm;
import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.articlecomment.ArticleComment;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.accompany.AccompanyArticleRepository;
import com.example.gabojago_server.repository.articlecomment.ArticleCommentRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final AccompanyArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleCommentRepository commentRepository;

    public List<CommentResponseDto> getAllComments(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
        List<ArticleComment> comments = commentRepository.findAllByArticle(article);

        if (comments.isEmpty())
            return Collections.emptyList();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return comments.stream()
                    .map(comment -> CommentResponseDto.of(comment, false))
                    .collect(Collectors.toList());
        } else {
            Member member = memberRepository.findById(Long.parseLong(authentication.getName()))
                    .orElseThrow();
            Map<Boolean, List<ArticleComment>> collect = comments.stream()
                    .collect(
                            Collectors.partitioningBy(
                                    comment -> comment.getWriter().equals(member)
                            )
                    );

            //로그인 유저가 작성한 comment들
            List<CommentResponseDto> trueCollect = collect.get(true).stream()
                    .map(t -> CommentResponseDto.of(t, true))
                    .collect(Collectors.toList());

            //로그인 유저가 작성하지 않은 comment들
            List<CommentResponseDto> falseCollect = collect.get(false).stream()
                    .map(f -> CommentResponseDto.of(f, false))
                    .collect(Collectors.toList());

            //둘 다 합쳐서 return
            return Stream.concat(trueCollect.stream(), falseCollect.stream())
                    .sorted(Comparator.comparing(CommentResponseDto::getCommentId))
                    .collect(Collectors.toList());
        }
    }

    @Alarm
    @Transactional
    public CommentResponseDto createComment(Long writerId, Long articleId, String content) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("글이 없습니다."));
        ArticleComment comment = ArticleComment.builder()
                .content(content)
                .article(article)
                .writer(member)
                .build();
        ArticleComment articleComment = commentRepository.save(comment);
        return CommentResponseDto.of(articleComment, true);
    }

    @Transactional
    public CommentResponseDto changeComment(Long writerId, Long commentId, String content) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));
        ArticleComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        if (!comment.getWriter().equals(member)) {
            throw new RuntimeException("댓글 작성자가 아닙니다.");
        }
        return CommentResponseDto.of(
                commentRepository.save(ArticleComment.update(comment, content)), true
        );
    }

    @Transactional
    public void removeComment(Long writerId, Long commentId) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));
        ArticleComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));
        if (!comment.getWriter().equals(member)) {
            throw new RuntimeException("댓글 작성자가 아닙니다.");
        }
        commentRepository.delete(comment);
    }
}
