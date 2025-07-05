package com.hilal.newsApp.repository;

import com.hilal.newsApp.entity.ExternalNewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalNewsRepository extends JpaRepository<ExternalNewsArticle,Long> {
    Optional<ExternalNewsArticle> findByNewsUrl(String newsUrl);
}
