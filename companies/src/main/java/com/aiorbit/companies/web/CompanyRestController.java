package com.aiorbit.companies.web;

import com.aiorbit.companies.dto.*;
import com.aiorbit.companies.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyRestController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<PagedResult<CompanySummaryDTO>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(companyService.list(category, search, sort, direction, page, size));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> categories() {
        return ResponseEntity.ok(companyService.listCategories());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CompanyDetailDTO> get(@PathVariable String slug) {
        return ResponseEntity.ok(companyService.getBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<CompanyDetailDTO> create(@Valid @RequestBody CompanyRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<CompanyDetailDTO> update(@PathVariable String slug,
                                                   @Valid @RequestBody CompanyRequestDTO request) {
        return ResponseEntity.ok(companyService.update(slug, request));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        companyService.delete(slug);
        return ResponseEntity.noContent().build();
    }
     @GetMapping("/suggest")
    public ResponseEntity<List<CompanySummaryDTO>> suggest(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(companyService.suggest(q, limit));
    }
}