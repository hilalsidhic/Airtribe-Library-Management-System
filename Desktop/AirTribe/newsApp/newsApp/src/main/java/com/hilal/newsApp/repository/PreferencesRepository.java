package com.hilal.newsApp.repository;

import com.hilal.newsApp.entity.NewsPreferences;
import com.hilal.newsApp.entity.dto.NewsPreferencesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends JpaRepository<NewsPreferences, Long> {
    NewsPreferencesDTO findByUserId(Long userId);
}
