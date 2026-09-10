package com.aiorbit.companies.service;

import com.aiorbit.companies.domain.Company;
import com.aiorbit.companies.dto.*;
import com.aiorbit.companies.exception.ResourceNotFoundException;
import com.aiorbit.companies.repository.CompanyRepository;
import com.aiorbit.companies.repository.CompanySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private static final int DEFAULT_SIZE = 9;

    private final CompanyRepository companyRepository;

    public PagedResult<CompanySummaryDTO> list(String category, String search,
                                               String sort, String direction,
                                               Integer page, Integer size) {

        Specification<Company> spec = CompanySpecifications.hasCategory(category)
                .and(CompanySpecifications.searchMatches(search));

        Pageable pageable = PageRequest.of(
                page == null || page < 0 ? 0 : page,
                size == null || size <= 0 ? DEFAULT_SIZE : Math.min(size, 50),
                buildSort(sort, direction));

        Page<CompanySummaryDTO> result = companyRepository.findAll(spec, pageable)
                .map(this::toSummary);

        return PagedResult.from(result);
    }

    public CompanyDetailDTO getBySlug(String slug) {
        return companyRepository.findBySlug(slug)
                .map(this::toDetail)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "slug", slug));
    }

    public List<CategoryDTO> listCategories() {
        return companyRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(Company::getCategory, java.util.LinkedHashMap::new, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .map(e -> CategoryDTO.builder().name(e.getKey()).count(e.getValue()).build())
                .toList();
    }

    @Transactional
    public CompanyDetailDTO create(CompanyRequestDTO request) {
        if (companyRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already in use: " + request.getSlug());
        }
        Company saved = companyRepository.save(toEntity(request));
        return toDetail(saved);
    }

    @Transactional
    public CompanyDetailDTO update(String slug, CompanyRequestDTO request) {
        Company existing = companyRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "slug", slug));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setCategory(request.getCategory());
        existing.setHeadquarters(request.getHeadquarters());
        existing.setFoundedYear(request.getFoundedYear());
        existing.setEmployees(request.getEmployees());
        existing.setWebsite(request.getWebsite());
        existing.setLogoUrl(request.getLogoUrl());
        existing.setRating(request.getRating());
        existing.setReviewCount(request.getReviewCount());
        existing.setVerified(request.getVerified());
        existing.setFeatured(request.getFeatured());
        return toDetail(companyRepository.save(existing));
    }

    @Transactional
    public void delete(String slug) {
        Company company = companyRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "slug", slug));
        companyRepository.delete(company);
    }

    private Sort buildSort(String sort, String direction) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String property = switch (sort == null ? "" : sort.toLowerCase()) {
            case "name" -> "name";
            case "founded" -> "foundedYear";
            case "newest" -> "createdAt";
            default -> "rating";
        };
        return Sort.by(dir, property).and(Sort.by(Sort.Direction.ASC, "name"));
    }

    private CompanySummaryDTO toSummary(Company c) {
        return CompanySummaryDTO.builder()
                .id(c.getId()).name(c.getName()).slug(c.getSlug())
                .category(c.getCategory()).headquarters(c.getHeadquarters())
                .logoUrl(c.getLogoUrl()).rating(c.getRating())
                .reviewCount(c.getReviewCount())
                .verified(c.getVerified()).featured(c.getFeatured())
                .build();
    }

    private CompanyDetailDTO toDetail(Company c) {
        return CompanyDetailDTO.builder()
                .id(c.getId()).name(c.getName()).slug(c.getSlug())
                .description(c.getDescription()).category(c.getCategory())
                .headquarters(c.getHeadquarters()).foundedYear(c.getFoundedYear())
                .employees(c.getEmployees()).website(c.getWebsite())
                .logoUrl(c.getLogoUrl()).rating(c.getRating())
                .reviewCount(c.getReviewCount())
                .verified(c.getVerified()).featured(c.getFeatured())
                .build();
    }

    private Company toEntity(CompanyRequestDTO r) {
        return Company.builder()
                .name(r.getName()).slug(r.getSlug()).description(r.getDescription())
                .category(r.getCategory()).headquarters(r.getHeadquarters())
                .foundedYear(r.getFoundedYear()).employees(r.getEmployees())
                .website(r.getWebsite()).logoUrl(r.getLogoUrl())
                .rating(r.getRating()).reviewCount(r.getReviewCount())
                .verified(Boolean.TRUE.equals(r.getVerified()))
                .featured(Boolean.TRUE.equals(r.getFeatured()))
                .build();
    }
     public List<CompanySummaryDTO> suggest(String q, Integer limit) {
        if (q == null || q.isBlank()) {
            return List.of();
        }
        int safeLimit = limit == null ? 5 : Math.min(Math.max(limit, 1), 10);
        Page<Company> page = companyRepository.findAll(
                CompanySpecifications.searchMatches(q.trim()),
                PageRequest.of(0, safeLimit, Sort.by(Sort.Direction.ASC, "name")));
        return page.map(this::toSummary).getContent();
    }
}