package com.hilal.newsApp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsApiResponseDTO {
    private String status;
    private int totalResults;
    private List<ExternalNewsArticleDTO> articles;
}
