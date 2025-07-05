package com.hilal.newsApp.controller;

import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.hilal.newsApp.entity.dto.NewsArticleReferenceDTO;
import com.hilal.newsApp.service.NewsService;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService _newsservice;

    @Autowired
    private UserService userService;

    @GetMapping("/read")
    public List<NewsArticleDTO> getNewsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return _newsservice.getNewsByUser(userId);
    }

    @PostMapping("/{Id}/read")
    public String addNewsArticle(@PathVariable("Id") Long newsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return _newsservice.addNewsArticle(userId, newsId);
    }

    @GetMapping("/favorites")
    public List<NewsArticleDTO> getFavsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return _newsservice.getNewsByUser(userId);
    }

    @PostMapping("/{Id}/favorite")
    public String addFavArticle(@PathVariable("Id") Long newsId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return _newsservice.addNewsArticle(userId, newsId);
    }

    @DeleteMapping("/{userId}/articles/{articleId}")
    public String removeNewsArticle(@PathVariable Long userId, @PathVariable Long articleId) {
        return _newsservice.removeNewsArticle(userId, articleId);
    }
}
