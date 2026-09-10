package com.aiorbit.companies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String category;
    private String headquarters;
    private Integer foundedYear;
    private String employees;
    private String website;
    private String logoUrl;
    private Double rating;
    private Integer reviewCount;
    private Boolean verified;
    private Boolean featured;
}