package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        Map<IssueStatus, Long> dbStats = issueService.countIssuesByStatus();

        Map<IssueStatus, Long> completeStats = new LinkedHashMap<>();
        for (IssueStatus status : IssueStatus.values()) {
            completeStats.put(status, dbStats.getOrDefault(status, 0L));
        }

        model.addAttribute("newIssuesNumber",
                dbStats.getOrDefault(IssueStatus.NEW, 0L));
        model.addAttribute("pendingIssuesNumber",
                dbStats.getOrDefault(IssueStatus.PENDING, 0L));
        model.addAttribute("assignedIssuesNumber",
                dbStats.getOrDefault(IssueStatus.ASSIGNED, 0L));
        model.addAttribute("inProgressIssuesNumber",
                dbStats.getOrDefault(IssueStatus.IN_PROGRESS, 0L));


        model.addAttribute("recentIssues", issueService.findByFilter(null, Pageable.ofSize(10)).getContent());

        return "dashboard";
    }
}