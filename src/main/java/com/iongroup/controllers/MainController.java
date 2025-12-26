package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

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
        // Fetch raw stats from service
        Map<IssueStatus, Long> dbStats = issueService.countIssuesByStatus();

        // Ensure all statuses are present in the map, even if count is 0
        // LinkedHashMap preserves insertion order (Order of Enum definition)
        Map<IssueStatus, Long> completeStats = new LinkedHashMap<>();
        for (IssueStatus status : IssueStatus.values()) {
            completeStats.put(status, dbStats.getOrDefault(status, 0L));
        }

        model.addAttribute("stats", completeStats);

        // Fetch recent issues for the grid
        model.addAttribute("recentIssues", issueService.findByFilter(null, org.springframework.data.domain.Pageable.ofSize(10)).getContent());

        return "dashboard";
    }
}