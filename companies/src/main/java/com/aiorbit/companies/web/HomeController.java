package com.aiorbit.companies.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToModule() {
        return "redirect:/companies";
    }
}
