package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.CreateIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final PosService posService;

    @GetMapping("/browse")
    public String browse(@RequestParam(required = false) String status,
                         @PageableDefault(size = 20) Pageable pageable,
                         Model model) {
        if (status != null && !status.isEmpty()) {
            model.addAttribute("issues", issueService.findByStatus(IssueStatus.valueOfSafe(status), pageable));
        } else {
            model.addAttribute("issues", issueService.findByFilter(null, pageable));
        }
        return "issues/browse";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("issue", new CreateIssueDto());
        // In a real app with many POS, you'd use an autocomplete/search API.
        // For now, we load a list to populate a <select>
        model.addAttribute("posList", posService.findByFilter(null, Pageable.unpaged()).getContent());
        return "issues/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CreateIssueDto issueDto) {
        issueService.create(issueDto);
        return "redirect:/issues/browse";
    }
}