package com.example.gabojago_server.service.article;

import com.example.gabojago_server.dto.response.article.qna.QnaResponseDto;
import com.example.gabojago_server.repository.article.Qna.QnaArticleRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnaService {
    private final QnaArticleRepository qnaArticleRepository;
    private final MemberRepository memberRepository;

    public QnaResponseDto oneQna(Long writerId, Long articleId) {
        return null;
    }

    @Transactional
    public QnaResponseDto postQna(Long wirterId, String title, String content) {
        return null;
    }

    @Transactional
    public QnaResponseDto changeQnaArticle(Long writerId, Long articleId, String title,
                                           String content, boolean isSelected) {
        return null;
    }

    @Transactional
    public void deleteQnaArticle(Long writerId, Long articleId) {

    }
}
