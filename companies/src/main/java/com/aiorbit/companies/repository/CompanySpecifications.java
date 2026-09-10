package com.aiorbit.companies.repository;

import com.aiorbit.companies.domain.Company;
import org.springframework.data.jpa.domain.Specification;

public final class CompanySpecifications {

    private CompanySpecifications() {}

    public static Specification<Company> hasCategory(String category) {
        return (root, query, cb) -> category == null || category.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Company> searchMatches(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("category")), pattern)
            );
        };
    }

    public static Specification<Company> isFeatured(boolean featured) {
        return (root, query, cb) -> cb.equal(root.get("featured"), featured);
    }
}