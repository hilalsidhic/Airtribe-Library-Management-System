package com.hilal.newsApp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CachedNewsResponse {
    private String id;
    private List<NewsArticleDTO> articles;
}
