package com.aiorbit.companies.web;

import com.aiorbit.companies.dto.CompanySummaryDTO;
import com.aiorbit.companies.dto.PagedResult;
import com.aiorbit.companies.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CompanyService companyService;

    @GetMapping("/")
    public String home() {
        return "redirect:/companies";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        PagedResult<CompanySummaryDTO> result =
                companyService.list(null, null, "rating", "desc", 0, 10);
        model.addAttribute("companies", result.getItems());
        return "pages/leaderboard";
    }

    @GetMapping("/learn")
    public String learn() {
        return "pages/learn";
    }
}