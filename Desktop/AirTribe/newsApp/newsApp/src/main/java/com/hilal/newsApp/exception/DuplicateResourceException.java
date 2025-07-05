package com.hilal.newsApp.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String newsArticleAlreadySaved) {
        super(newsArticleAlreadySaved);
    }
}
