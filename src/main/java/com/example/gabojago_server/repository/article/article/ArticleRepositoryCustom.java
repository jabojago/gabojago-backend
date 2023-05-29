package com.example.gabojago_server.repository.article.article;

import com.example.gabojago_server.dto.response.article.community.PageArticleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepositoryCustom {
    Page<PageArticleResponseDto> searchAll(Pageable pageable);
}
