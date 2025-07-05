package com.hilal.newsApp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalNewsArticleDTO {
    private String author;
    private String title;
    private String description; // maps to your 'content' later
    private String url; // maps to 'newsUrl'
    private String urlToImage; // maps to 'imageUrl'
    private String publishedAt;
    private String content;
    private SourcesDTO source;
}
