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

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IssueService issueService;

    @GetMapping
    public String showDashboard(@RequestParam(required = false) String status,
                                @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {
        // Fetch issues with the same logic as the browse page
        if (status != null && !status.isEmpty()) {
            model.addAttribute("issues", issueService.findByStatus(IssueStatus.valueOfSafe(status), pageable));
        } else {
            model.addAttribute("issues", issueService.findByFilter(null, pageable));
        }

        // Add stats for the cards (assuming these methods exist or were populated previously)
        var stats = issueService.countIssuesByStatus();
        model.addAttribute("newIssuesNumber", stats.getOrDefault(IssueStatus.NEW, 0L));
        model.addAttribute("pendingIssuesNumber", stats.getOrDefault(IssueStatus.PENDING, 0L));
        model.addAttribute("assignedIssuesNumber", stats.getOrDefault(IssueStatus.ASSIGNED, 0L));
        model.addAttribute("inProgressIssuesNumber", stats.getOrDefault(IssueStatus.IN_PROGRESS, 0L));

        return "dashboard";
    }
}