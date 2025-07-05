package com.hilal.newsApp.service;

import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ExternalNewsService {
    Mono<List<NewsArticleDTO>> fetchNewsByKeyword(String keyword, Long userId);

    Mono<List<NewsArticleDTO>> fetchNewsByPreferences(Long userId);
}
