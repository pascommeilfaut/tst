// tst/src/main/java/com/iongroup/controllers/DashboardController.java
package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IssueService issueService;

    @GetMapping
    public String showDashboard(Model model) {
        // Stats
        Map<IssueStatus, Long> counts = issueService.countIssuesByStatus();
        model.addAttribute("newIssuesNumber", counts.getOrDefault(IssueStatus.NEW, 0L));
        model.addAttribute("pendingIssuesNumber", counts.getOrDefault(IssueStatus.PENDING, 0L));
        model.addAttribute("assignedIssuesNumber", counts.getOrDefault(IssueStatus.ASSIGNED, 0L));
        model.addAttribute("inProgressIssuesNumber", counts.getOrDefault(IssueStatus.IN_PROGRESS, 0L));

        // Issues List
        // Fetching the first page of issues for the dashboard
        model.addAttribute("issues", issueService.findByFilter(null,
                PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt")));

        return "dashboard";
    }
}