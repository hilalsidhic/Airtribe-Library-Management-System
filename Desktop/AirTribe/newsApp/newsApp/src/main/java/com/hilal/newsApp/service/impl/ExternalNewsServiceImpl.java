package com.hilal.newsApp.service.impl;

import com.hilal.newsApp.entity.Category;
import com.hilal.newsApp.entity.ExternalNewsArticle;
import com.hilal.newsApp.entity.dto.NewsApiResponseDTO;
import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.hilal.newsApp.entity.dto.NewsPreferencesDTO;
import com.hilal.newsApp.repository.ExternalNewsRepository;
import com.hilal.newsApp.service.ExternalNewsService;
import com.hilal.newsApp.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalNewsServiceImpl implements ExternalNewsService {

    @Value("${newsapi.apiKey}")
    private String API_KEY;

    @Autowired
    @Qualifier("externalNewsApiWebClient")
    private WebClient externalNewsApiWebClient;

    @Autowired
    private ReactiveRedisTemplate<String, List<NewsArticleDTO>> redisTemplate;

    @Autowired
    private PreferenceService _preferenceService;

    @Autowired
    private ExternalNewsRepository externalNewsArticleRepository;

    @Override
    public Mono<List<NewsArticleDTO>> fetchNewsByKeyword(String keyword, Long userId) {

        System.out.println("Fetching news by keyword: " + keyword);
        String cacheKey = String.format("news:preferences:%s", keyword);

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .doOnNext(val -> System.out.println("Redis returned: " + val))
                .flatMap(cachedList -> {
                    System.out.println("✅ Cache HIT for userId " + userId);
                    return Mono.just(cachedList);
                })
                .switchIfEmpty(
                        Mono.defer(() -> {
                            return externalNewsApiWebClient.get()
                                    .uri(uriBuilder -> uriBuilder
                                            .scheme("https")
                                            .host("newsapi.org")
                                            .path("/v2/everything")
                                            .queryParam("q", keyword.isEmpty() ? "news" : keyword)
                                            .queryParam("pageSize", 10)
                                            .build())
                                    .header("X-Api-Key", API_KEY)
                                    .retrieve()
                                    .bodyToMono(NewsApiResponseDTO.class)
                                    .flatMap(response -> {
                                        List<NewsArticleDTO> articles = Optional.ofNullable(response.getArticles())
                                                .orElse(List.of())
                                                .stream()
                                                .map(article -> NewsArticleDTO.builder()
                                                        .title(article.getTitle())
                                                        .content(article.getDescription())
                                                        .author(article.getAuthor())
                                                        .source(article.getSource() != null ? article.getSource().getName() : null)
                                                        .publishedAt(article.getPublishedAt() != null && article.getPublishedAt().length() >= 19
                                                                ? LocalDateTime.parse(article.getPublishedAt().substring(0, 19))
                                                                : null)
                                                        .newsUrl(article.getUrl())
                                                        .imageUrl(article.getUrlToImage())
                                                        .category(null)
                                                        .build())
                                                .limit(10)
                                                .toList();

                                        return Mono.fromCallable(() -> {
                                                    return articles.stream().map(dto -> {
                                                        ExternalNewsArticle entity = externalNewsArticleRepository.findByNewsUrl(dto.getNewsUrl())
                                                                .orElseGet(() -> {
                                                                    ExternalNewsArticle newEntity = new ExternalNewsArticle();
                                                                    newEntity.setTitle(dto.getTitle());
                                                                    newEntity.setContent(dto.getContent());
                                                                    newEntity.setAuthor(dto.getAuthor());
                                                                    newEntity.setSource(dto.getSource());
                                                                    newEntity.setPublishedAt(dto.getPublishedAt());
                                                                    newEntity.setNewsUrl(dto.getNewsUrl());
                                                                    newEntity.setImageUrl(dto.getImageUrl());
                                                                    return externalNewsArticleRepository.save(newEntity);
                                                                });

                                                        // 🟢 Add the DB ID to the DTO
                                                        dto.setId(entity.getId());

                                                        return dto;
                                                    }).toList();
                                                })
                                                .subscribeOn(Schedulers.boundedElastic())
                                                .flatMap(savedArticles ->
                                                        redisTemplate.opsForValue()
                                                                .set(cacheKey, savedArticles, Duration.ofMinutes(30))
                                                                .doOnSuccess(result -> System.out.println("✅ Redis saved: " + result))
                                                                .thenReturn(savedArticles)
                                                );

                                    });
                        })
                );
    }
//    @Override
//    public Mono<List<NewsArticleDTO>> fetchNewsByKeyword(String keyword) {
//        System.out.println("Fetching news by keyword: " + keyword);
//        return externalNewsApiWebClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .host("newsapi.org")
//                        .path("/v2/everything")
//                        .queryParam("q", (keyword == null || keyword.isBlank()) ? "news" : keyword)
//                        .build())
//                .headers(headers -> {
//                    headers.set("X-Api-Key", API_KEY);
//                    headers.remove("Authorization");
//                })
//                .retrieve()
//                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                        clientResponse -> clientResponse.bodyToMono(String.class)
//                                .map(body -> new RuntimeException("External API error: " + body)))
//                .bodyToMono(NewsApiResponseDTO.class)
//                .map(response -> Optional.ofNullable(response.getArticles())
//                        .orElse(List.of())
//                        .stream()
//                        .map(article -> {
//                            return NewsArticleDTO.builder()
//                                    .title(article.getTitle())
//                                    .content(article.getDescription())
//                                    .author(article.getAuthor())
//                                    .source(article.getSource() != null ? article.getSource().getName() : null)
//                                    .publishedAt(article.getPublishedAt() != null && article.getPublishedAt().length() >= 19
//                                            ? LocalDateTime.parse(article.getPublishedAt().substring(0, 19))
//                                            : null)
//                                    .newsUrl(article.getUrl())
//                                    .imageUrl(article.getUrlToImage())
//                                    .category(null) // maybe fill default or map later
//                                    .build();
//                        }).toList());
//    }
    public Mono<List<NewsArticleDTO>> fetchNewsByPreferences(Long userId) {
        System.out.println("Fetching news by preferences for userId: " + userId);
        System.out.println("⏳ Cache MISS for userId " + userId);

        NewsPreferencesDTO preferences = _preferenceService.getPreferences(userId);
        String keyword = preferences.getPreferredCategories() != null && !preferences.getPreferredCategories().isEmpty()
                ? String.join(" OR ", preferences.getPreferredCategories().stream().map(Category::name).toList())
                : "";
        String sources = preferences.getPreferredSources() != null && !preferences.getPreferredSources().isEmpty()
                ? String.join(",", preferences.getPreferredSources())
                : null;
        String language = preferences.getPreferredLanguage();

        String keywordPart = preferences.getPreferredCategories() != null && !preferences.getPreferredCategories().isEmpty()
                ? String.join("_", preferences.getPreferredCategories().stream().map(Category::name).sorted().toList())
                : "all";

        String sourcesPart = preferences.getPreferredSources() != null && !preferences.getPreferredSources().isEmpty()
                ? String.join("_", preferences.getPreferredSources().stream().sorted().toList())
                : "any";

        String languagePart = (preferences.getPreferredLanguage() != null && !preferences.getPreferredLanguage().isBlank())
                ? preferences.getPreferredLanguage()
                : "any";

        String cacheKey = String.format("news:preferences:%s:%s:%s",
                keywordPart, sourcesPart, languagePart);

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .doOnNext(val -> System.out.println("Redis returned: " + val))
                .flatMap(cachedList -> {
                    System.out.println("✅ Cache HIT for userId " + userId);
                    return Mono.just(cachedList);
                })
                .switchIfEmpty(
                        Mono.defer(() -> {
                            return externalNewsApiWebClient.get()
                                    .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host("newsapi.org")
                                    .path("/v2/everything")
                                    .queryParam("q", keyword.isEmpty() ? "news" : keyword)
                                    .queryParamIfPresent("sources", Optional.ofNullable(sources).filter(s -> !s.isEmpty()))
                                    .queryParamIfPresent("language", Optional.ofNullable(language).filter(l -> !l.isEmpty()))
                                    .queryParam("pageSize", 10)
                                    .build())
                                    .header("X-Api-Key", API_KEY)
                                    .retrieve()
                                    .bodyToMono(NewsApiResponseDTO.class)
                                    .flatMap(response -> {
                                        List<NewsArticleDTO> articles = Optional.ofNullable(response.getArticles())
                                                .orElse(List.of())
                                                .stream()
                                                .map(article -> NewsArticleDTO.builder()
                                                        .title(article.getTitle())
                                                        .content(article.getDescription())
                                                        .author(article.getAuthor())
                                                        .source(article.getSource() != null ? article.getSource().getName() : null)
                                                        .publishedAt(article.getPublishedAt() != null && article.getPublishedAt().length() >= 19
                                                                ? LocalDateTime.parse(article.getPublishedAt().substring(0, 19))
                                                                : null)
                                                        .newsUrl(article.getUrl())
                                                        .imageUrl(article.getUrlToImage())
                                                        .category(null)
                                                        .build())
                                                .limit(10)
                                                .toList();

                                        return Mono.fromCallable(() -> {
                                                    return articles.stream().map(dto -> {
                                                        ExternalNewsArticle entity = externalNewsArticleRepository.findByNewsUrl(dto.getNewsUrl())
                                                                .orElseGet(() -> {
                                                                    ExternalNewsArticle newEntity = new ExternalNewsArticle();
                                                                    newEntity.setTitle(dto.getTitle());
                                                                    newEntity.setContent(dto.getContent());
                                                                    newEntity.setAuthor(dto.getAuthor());
                                                                    newEntity.setSource(dto.getSource());
                                                                    newEntity.setPublishedAt(dto.getPublishedAt());
                                                                    newEntity.setNewsUrl(dto.getNewsUrl());
                                                                    newEntity.setImageUrl(dto.getImageUrl());
                                                                    return externalNewsArticleRepository.save(newEntity);
                                                                });

                                                        // 🟢 Add the DB ID to the DTO
                                                        dto.setId(entity.getId());

                                                        return dto;
                                                    }).toList();
                                                })
                                                .subscribeOn(Schedulers.boundedElastic())
                                                .flatMap(savedArticles ->
                                                        redisTemplate.opsForValue()
                                                                .set(cacheKey, savedArticles, Duration.ofMinutes(30))
                                                                .doOnSuccess(result -> System.out.println("✅ Redis saved: " + result))
                                                                .thenReturn(savedArticles)
                                                );

                                    });
                        })
                );
    }


}