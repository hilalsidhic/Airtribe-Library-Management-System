package com.hilal.newsApp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> role;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NewsPreferences newsPreferences;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private Set<NewsArticle> savedArticles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private Set<NewsArticle> favArticles = new HashSet<>();

    public void addSavedArticle(NewsArticle newsArticle) {
        savedArticles.add(newsArticle);
        newsArticle.setUser(this);
    }

    public void addFavArticle(NewsArticle newsArticle) {
        favArticles.add(newsArticle);
        newsArticle.setUser(this);
    }

}