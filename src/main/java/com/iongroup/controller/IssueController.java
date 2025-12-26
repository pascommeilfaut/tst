package com.iongroup.controller;

import com.iongroup.data.issue.IssuePriority;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.data.user.UserType;
import com.iongroup.service.IssueService;
import com.iongroup.service.IssueTypeService;
import com.iongroup.service.PosService;
import com.iongroup.service.UserService;
import com.iongroup.service.dto.CreateIssueDto;
import com.iongroup.service.mapper.IssueTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueTypeService issueTypeService;
    private final PosService posService;
    private final UserService userService;
    private final IssueTypeMapper issueTypeMapper;

    @GetMapping
    public String browse(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("pageTitle", "Browse Issues");
        model.addAttribute("issues", issueService.findByFilter(search, Pageable.unpaged()));
        return "issues/browse";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("pageTitle", "Add Issue");
        model.addAttribute("issue", new CreateIssueDto());
        populateDropdowns(model);
        return "issues/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("pageTitle", "Edit Issue");
        CreateIssueDto dto = issueService.findSaveIssueDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + id));
        model.addAttribute("issue", dto);
        // Needed for update logic to distinguish between create/update
        model.addAttribute("issueId", id);
        populateDropdowns(model);
        return "issues/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CreateIssueDto issueDto, @RequestParam(required = false) Integer id) {
        if (id == null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            // Assuming the logged in user exists
            issueDto.setCreatedBy(userService.findByLogin(login).orElseThrow().getId());
            issueService.create(issueDto);
        } else {
            issueService.update(issueDto, id);
        }
        return "redirect:/issues";
    }

    private void populateDropdowns(Model model) {
        model.addAttribute("posList", posService.findByFilter(null, Pageable.unpaged()).getContent());
        model.addAttribute("types", issueTypeService.findAllParents(Pageable.unpaged()).map(issueTypeMapper::mapToDto).getContent());
        model.addAttribute("subTypes", issueTypeService.findAllSubTypes(Pageable.unpaged()).map(issueTypeMapper::mapToDto).getContent());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("userTypes", UserType.values());
    }
}