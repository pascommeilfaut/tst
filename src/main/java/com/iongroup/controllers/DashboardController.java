package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IssueService issueService;

    @GetMapping
    public String showDashboard(
            @RequestParam(required = false) String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Map<IssueStatus, Long> counts = issueService.countIssuesByStatus();
        model.addAttribute("newIssuesNumber", counts.getOrDefault(IssueStatus.NEW, 0L));
        model.addAttribute("pendingIssuesNumber", counts.getOrDefault(IssueStatus.PENDING, 0L));
        model.addAttribute("assignedIssuesNumber", counts.getOrDefault(IssueStatus.ASSIGNED, 0L));
        model.addAttribute("inProgressIssuesNumber", counts.getOrDefault(IssueStatus.IN_PROGRESS, 0L));

        model.addAttribute("issues", issueService.findByFilter(searchTerm, pageable));
        model.addAttribute("currentUrl", "/dashboard");

        return "dashboard";
    }
}