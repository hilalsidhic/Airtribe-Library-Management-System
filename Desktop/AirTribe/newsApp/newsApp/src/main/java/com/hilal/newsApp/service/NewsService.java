package com.hilal.newsApp.service;

import com.hilal.newsApp.entity.Category;
import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.hilal.newsApp.entity.dto.NewsArticleReferenceDTO;
import com.hilal.newsApp.exception.DuplicateResourceException;
import com.hilal.newsApp.exception.ResourceNotFoundException;

import java.util.List;


public interface NewsService {
    List<NewsArticleDTO> getNewsByUser(Long userId) throws ResourceNotFoundException;
    String addNewsArticle(Long userId, Long newsId) throws DuplicateResourceException;
    String removeNewsArticle(Long userId, Long articleId) throws ResourceNotFoundException;

    List<NewsArticleDTO> getFavsByUser(Long userId) throws ResourceNotFoundException;
    String addFavArticle(Long userId, Long newsId) throws DuplicateResourceException;

}
