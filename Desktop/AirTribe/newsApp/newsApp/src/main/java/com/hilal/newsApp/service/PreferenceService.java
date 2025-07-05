package com.hilal.newsApp.service;

import com.hilal.newsApp.entity.dto.NewsPreferencesDTO;


public interface PreferenceService {
    NewsPreferencesDTO savePreferences(Long userId, NewsPreferencesDTO preferencesDTO);
    NewsPreferencesDTO updatePreferences(Long userId, NewsPreferencesDTO preferencesDTO);
    String deletePreferences(Long userId);
    NewsPreferencesDTO getPreferences(Long userId);
    NewsPreferencesDTO setLanguage(Long userId, String language);
}
