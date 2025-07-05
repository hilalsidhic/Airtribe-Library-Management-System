package com.hilal.newsApp.controller;

import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.hilal.newsApp.service.ExternalNewsService;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExternalNewsController {

    @Autowired
    private ExternalNewsService externalNewsService;

    @Autowired
    private UserService userService;

    @GetMapping("/news/search/{keyword}")
    public Mono<List<NewsArticleDTO>> getNewsByKeyword(@PathVariable String keyword) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
       Long userId = userService.getUserDetails(username).getId();
       return externalNewsService.fetchNewsByKeyword(keyword,userId);
    }

    @GetMapping("/news")
    public Mono<List<NewsArticleDTO>> getNewsByPreferences() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return externalNewsService.fetchNewsByPreferences(userId);
    }
}
