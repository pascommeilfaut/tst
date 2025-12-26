package com.iongroup.controllers;

import com.iongroup.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final IssueService issueService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("stats", issueService.countIssuesByStatus());
        model.addAttribute("recentIssues", issueService.findByFilter(null, org.springframework.data.domain.Pageable.ofSize(10)).getContent());
        return "dashboard";
    }
}