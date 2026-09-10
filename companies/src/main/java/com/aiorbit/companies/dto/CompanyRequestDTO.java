package com.aiorbit.companies.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$", message = "Slug must be lowercase, hyphen-separated")
    private String slug;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private String headquarters;

    @Min(value = 1900, message = "Founded year must be 1900 or later")
    private Integer foundedYear;

    private String employees;

    private String website;

    private String logoUrl;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double rating;

    @PositiveOrZero
    private Integer reviewCount;

    private Boolean verified;

    private Boolean featured;
}