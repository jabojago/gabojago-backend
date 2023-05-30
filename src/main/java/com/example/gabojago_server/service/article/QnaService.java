package com.example.gabojago_server.service.article;

import com.example.gabojago_server.dto.response.article.qna.PageQnaResponseDto;
import com.example.gabojago_server.dto.response.article.qna.QnaResponseDto;
import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.model.article.QnaArticle;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.article.Qna.QnaArticleRepository;
import com.example.gabojago_server.service.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {
    private final QnaArticleRepository qnaArticleRepository;
    private final EntityFinder entityFinder;

    public QnaResponseDto oneQna(Long writerId, Long articleId) {
        QnaArticle article = entityFinder.findQnaArticle(articleId);
        article.reviewCountUp();
        if (isOwner(writerId, articleId)) return QnaResponseDto.of(article, true);
        else return QnaResponseDto.of(article, false);
    }

    private boolean isOwner(Long writerId, Long articleId) {
        if (writerId == null) return false;
        return qnaArticleRepository.existArticleByWriter(writerId, articleId);
    }

    public Page<PageQnaResponseDto> allQna(Pageable pageable) {
        return qnaArticleRepository
                .searchAll(pageable);
    }

    @Transactional
    public QnaResponseDto postQna(Long writerId, String title, String content, boolean selected) {
        Member writer = entityFinder.findMember(writerId);
        QnaArticle article = QnaArticle.createQnaArticle(writer, title, content, 0, selected);
        return QnaResponseDto.of(qnaArticleRepository.save(article), true);
    }

    @Transactional
    public QnaResponseDto changeQnaArticle(Long writerId, Long articleId, String title,
                                           String content, boolean selected) {
        QnaArticle article = authorizationQnaWriter(writerId, articleId);
        return QnaResponseDto.of(
                qnaArticleRepository.save(
                        QnaArticle.update(
                                article, title, content, selected
                        )
                ), true
        );
    }

    @Transactional
    public void deleteQnaArticle(Long writerId, Long articleId) {
        QnaArticle article = authorizationQnaWriter(writerId, articleId);
        qnaArticleRepository.delete(article);
    }

    private QnaArticle authorizationQnaWriter(Long writerId, Long articleId) {
        Member member = entityFinder.findMember(writerId);
        QnaArticle article = entityFinder.findQnaArticle(articleId);
        if (!article.getWriter().equals(member)) throw new GabojagoException(ErrorCode.FORBIDDEN_ARTICLE);
        return article;
    }
}
