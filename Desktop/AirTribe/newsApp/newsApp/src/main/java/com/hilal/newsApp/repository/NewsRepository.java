package com.hilal.newsApp.repository;

import com.hilal.newsApp.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsArticle, Long> {
}
