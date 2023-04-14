package com.example.gabojago_server.repository.article;

import com.example.gabojago_server.dto.response.article.PageAccompanyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepositoryCustom {
    Page<PageAccompanyResponseDto> searchAll(Pageable pageable);
}
