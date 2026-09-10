package com.aiorbit.companies.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "companies", indexes = {
    @Index(name = "idx_company_slug", columnList = "slug", unique = true),
    @Index(name = "idx_company_category", columnList = "category"),
    @Index(name = "idx_company_rating", columnList = "rating")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slug;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(nullable = false)
    private String category;

    private String headquarters;

    @Column(name = "founded_year")
    private Integer foundedYear;

    private String employees;

    private String website;

    @Column(name = "logo_url")
    private String logoUrl;

    @PositiveOrZero
    private Double rating;

    @Column(name = "review_count")
    private Integer reviewCount;

    private Boolean verified;

    private Boolean featured;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}