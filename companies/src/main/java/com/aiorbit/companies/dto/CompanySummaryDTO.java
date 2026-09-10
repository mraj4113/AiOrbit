package com.aiorbit.companies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySummaryDTO {
    private Long id;
    private String name;
    private String slug;
    private String category;
    private String headquarters;
    private String logoUrl;
    private Double rating;
    private Integer reviewCount;
    private Boolean verified;
    private Boolean featured;
}