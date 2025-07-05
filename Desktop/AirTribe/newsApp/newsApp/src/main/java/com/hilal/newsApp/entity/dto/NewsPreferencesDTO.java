package com.hilal.newsApp.entity.dto;

import com.hilal.newsApp.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsPreferencesDTO {
    private Long userId;
    private String username;
    private String preferredLanguage;
    private Set<Category> preferredCategories;
    private Set<String> preferredSources;
    private Set<String> preferredCountries;
}