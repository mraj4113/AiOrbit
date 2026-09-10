package com.aiorbit.companies.repository;

import com.aiorbit.companies.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findBySlug(String slug);

    boolean existsBySlug(String slug);
}