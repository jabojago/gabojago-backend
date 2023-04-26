package com.example.gabojago_server.repository.article.Qna;

import com.example.gabojago_server.dto.response.article.qna.PageQnaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaArticleRepositoryCustom {
    Page<PageQnaResponseDto> searchAll(Pageable pageable);
}
