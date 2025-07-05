package com.hilal.newsApp.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hilal.newsApp.entity.dto.NewsArticleDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, List<NewsArticleDTO>> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        // Create ObjectMapper and precise CollectionType
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // ✅ this is crucial
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, NewsArticleDTO.class);

        Jackson2JsonRedisSerializer<List<NewsArticleDTO>> valueSerializer =
                new Jackson2JsonRedisSerializer<>(listType);

        valueSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        RedisSerializationContext.RedisSerializationContextBuilder<String, List<NewsArticleDTO>> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, List<NewsArticleDTO>> context =
                builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(  factory);
        return template;
    }
}
