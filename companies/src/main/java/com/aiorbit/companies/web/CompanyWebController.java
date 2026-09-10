package com.aiorbit.companies.web;

import com.aiorbit.companies.dto.CategoryDTO;
import com.aiorbit.companies.dto.CompanyDetailDTO;
import com.aiorbit.companies.dto.CompanySummaryDTO;
import com.aiorbit.companies.dto.PagedResult;
import com.aiorbit.companies.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyWebController {

    private final CompanyService companyService;

    @GetMapping
    public String listPage(@RequestParam(required = false) String category,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "rating") String sort,
                           @RequestParam(required = false, defaultValue = "desc") String direction,
                           @RequestParam(required = false, defaultValue = "0") Integer page,
                           Model model) {

        PagedResult<CompanySummaryDTO> result =
                companyService.list(category, search, sort, direction, page, 9);
        List<CategoryDTO> categories = companyService.listCategories();

        model.addAttribute("result", result);
        model.addAttribute("categories", categories);
        model.addAttribute("activeCategory", category == null ? "" : category);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        return "companies/list";
    }

    @GetMapping("/{slug}")
    public String detailPage(@PathVariable String slug, Model model) {
        CompanyDetailDTO company = companyService.getBySlug(slug);
        model.addAttribute("company", company);
        return "companies/detail";
    }
}