package com.iongroup.controllers;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.IssueService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.CreateIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                         @RequestParam(required = false) String searchTerm,
                         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            model.addAttribute("issues", issueService.findByFilter(searchTerm, pageable));
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("issues", issueService.findByStatus(IssueStatus.valueOfSafe(status), pageable));
        } else {
            model.addAttribute("issues", issueService.findByFilter(null, pageable));
        }
        return "issues/browse";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("issue", new CreateIssueDto());
        model.addAttribute("posList", posService.findByFilter(null, Pageable.unpaged()).getContent());
        model.addAttribute("formAction", "/issues/save");
        return "issues/add";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        CreateIssueDto dto = issueService.findSaveIssueDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + id));
        model.addAttribute("issue", dto);
        model.addAttribute("posList", posService.findByFilter(null, Pageable.unpaged()).getContent());
        model.addAttribute("formAction", "/issues/update/" + id);
        return "issues/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CreateIssueDto issueDto) {
        issueService.create(issueDto);
        return "redirect:/issues/browse";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute CreateIssueDto issueDto) {
        issueService.update(issueDto, id);
        return "redirect:/issues/browse";
    }
}