package com.example.gabojago_server.repository.article.accompany;

import com.example.gabojago_server.dto.response.article.accompany.PageAccompanyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyArticleRepositoryCustom {
    Page<PageAccompanyResponseDto> searchAll(Pageable pageable);
}
