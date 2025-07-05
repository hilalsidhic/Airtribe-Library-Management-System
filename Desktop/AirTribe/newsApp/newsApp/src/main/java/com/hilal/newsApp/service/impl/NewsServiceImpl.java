package com.hilal.newsApp.service.impl;

import com.hilal.newsApp.entity.ExternalNewsArticle;
import com.hilal.newsApp.entity.NewsArticle;
import com.hilal.newsApp.entity.User;
import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.hilal.newsApp.entity.dto.NewsArticleReferenceDTO;
import com.hilal.newsApp.exception.DuplicateResourceException;
import com.hilal.newsApp.exception.ResourceNotFoundException;
import com.hilal.newsApp.repository.ExternalNewsRepository;
import com.hilal.newsApp.repository.NewsRepository;
import com.hilal.newsApp.repository.UserRepository;
import com.hilal.newsApp.service.ExternalNewsService;
import com.hilal.newsApp.service.NewsService;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {


    @Autowired
    private ExternalNewsRepository _externalNewsRepository;

    @Autowired
    private NewsRepository _newsRepository;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private UserService _userService;

    @Override
    public List<NewsArticleDTO> getNewsByUser(Long userId) throws ResourceNotFoundException {
        User user = _userService.getUserEntityById(userId);
        if(user.getSavedArticles().isEmpty()||user.getNewsPreferences() == null) {
            throw new ResourceNotFoundException("No saved articles or preferences found for user");
        }
        return user.getSavedArticles().stream()
                .map(article -> NewsArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .source(article.getSource())
                .publishedAt(article.getPublishedAt())
                .author(article.getAuthor())
                .newsUrl(article.getNewsUrl())
                .imageUrl(article.getImageUrl())
                .build())
                .toList();
    }

    @Override
    public String addNewsArticle(Long userId, Long newsId) throws DuplicateResourceException {
        User user = _userService.getUserEntityById(userId);
        System.out.println("User: " + user.getUsername() + ", News ID: " + newsId);
        ExternalNewsArticle externalNewsArticle = _externalNewsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News article does not exist"));

        if (user.getSavedArticles().stream().anyMatch(article -> article.getReferenceId().equals(newsId))) {
            throw new DuplicateResourceException("News article already saved");
        }
        NewsArticle newsArticle = NewsArticle.builder()
                .referenceId(newsId)
                .title(externalNewsArticle.getTitle())
                .content(externalNewsArticle.getContent())
                .source(externalNewsArticle.getSource())
                .publishedAt(externalNewsArticle.getPublishedAt())
                .author(externalNewsArticle.getAuthor())
                .newsUrl(externalNewsArticle.getNewsUrl())
                .imageUrl(externalNewsArticle.getImageUrl())
                .build();
        user.addSavedArticle(newsArticle);
        _userRepository.save(user);
        return "News article saved successfully";
    }

    @Override
    public String removeNewsArticle(Long userId, Long articleId) throws ResourceNotFoundException {
        User user = _userService.getUserEntityById(userId);
        if(user.getSavedArticles().isEmpty()){
            throw new ResourceNotFoundException("No saved articles found for user");
        }
        if(user.getSavedArticles().stream().noneMatch(article -> article.getId().equals(articleId))) {
            throw new ResourceNotFoundException("News article not found in saved articles");
        }
        user.getSavedArticles().removeIf(article -> article.getId().equals(articleId));
        _userRepository.save(user);
        return "News article removed successfully";
    }

    @Override
    public List<NewsArticleDTO> getFavsByUser(Long userId) throws ResourceNotFoundException {
        User user = _userService.getUserEntityById(userId);
        if(user.getFavArticles().isEmpty()||user.getNewsPreferences() == null) {
            throw new ResourceNotFoundException("No saved articles or preferences found for user");
        }
        return user.getFavArticles().stream()
                .map(article -> NewsArticleDTO.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .source(article.getSource())
                        .publishedAt(article.getPublishedAt())
                        .author(article.getAuthor())
                        .newsUrl(article.getNewsUrl())
                        .imageUrl(article.getImageUrl())
                        .build())
                .toList();
    }

    @Override
    public String addFavArticle(Long userId, Long newsId) throws DuplicateResourceException {
        User user = _userService.getUserEntityById(userId);
        System.out.println("User: " + user.getUsername() + ", News ID: " + newsId);
        ExternalNewsArticle externalNewsArticle = _externalNewsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News article does not exist"));

        if (user.getFavArticles().stream().anyMatch(article -> article.getReferenceId().equals(newsId))) {
            throw new DuplicateResourceException("News article already saved");
        }
        NewsArticle newsArticle = NewsArticle.builder()
                .referenceId(newsId)
                .title(externalNewsArticle.getTitle())
                .content(externalNewsArticle.getContent())
                .source(externalNewsArticle.getSource())
                .publishedAt(externalNewsArticle.getPublishedAt())
                .author(externalNewsArticle.getAuthor())
                .newsUrl(externalNewsArticle.getNewsUrl())
                .imageUrl(externalNewsArticle.getImageUrl())
                .build();
        user.addFavArticle(newsArticle);
        _userRepository.save(user);
        return "News article saved successfully";
    }
}
