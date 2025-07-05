        package com.hilal.newsApp.entity;

        import com.fasterxml.jackson.annotation.JsonBackReference;
        import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotNull;
        import lombok.*;

        import java.time.LocalDateTime;

        @Entity
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public class NewsArticle {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            private Long referenceId;

            private String title;

            @Column(columnDefinition = "TEXT")
            private String content;

            private String author;

            private String source;

            private LocalDateTime publishedAt;

            private String newsUrl;

            private String imageUrl;

            @ManyToOne(fetch = FetchType.EAGER)
            @JoinColumn(name = "user_id")
            @JsonBackReference
            @ToString.Exclude
            private User user;
        }
