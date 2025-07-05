package com.hilal.newsApp.entity.dto;

import com.hilal.newsApp.entity.Category;
import com.hilal.newsApp.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String source;
    private LocalDateTime publishedAt;
    private String newsUrl;
    private String imageUrl;
    private Category category;
}
