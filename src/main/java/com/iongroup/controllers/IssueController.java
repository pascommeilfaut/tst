package com.iongroup.controllers;

import com.iongroup.data.issue.IssuePriority;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.data.user.UserType;
import com.iongroup.service.IssueService;
import com.iongroup.service.IssueTypeService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.CreateIssueDto;
import com.iongroup.service.dto.UpdateIssueDto;
import com.iongroup.service.mapper.IssueMapper;
import com.iongroup.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueTypeService issueTypeService;
    private final PosService posService;
    private final IssueMapper issueMapper;

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
        populateFormAttributes(model);
        model.addAttribute("formAction", "/issues/save");
        return "issues/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("issue") CreateIssueDto issueDto,
                       BindingResult bindingResult,
                       Model model) {
        issueDto.setCreatedBy(AuthUtils.getCurrentUser().getId());
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            model.addAttribute("formAction", "/issues/save");
            return "issues/add";
        }

        try {
            issueService.create(issueDto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("globalError", e.getMessage());
            populateFormAttributes(model);
            model.addAttribute("formAction", "/issues/save");
            return "issues/add";
        }

        return "redirect:/issues/browse";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        UpdateIssueDto dto = issueService.findById(id)
                .map(issueMapper::mapToUpdateDto)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found: " + id));

        model.addAttribute("issue", dto);
        populateFormAttributes(model);
        model.addAttribute("formAction", "/issues/update/%s".formatted(id));
        return "issues/add";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("posDto") UpdateIssueDto issueDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return editForm(id, model);
        }
        issueService.update(issueDto, id);
        return "redirect:/issues/browse";
    }

    private void populateFormAttributes(Model model) {
        model.addAttribute("posList", posService.findByFilter(null, Pageable.unpaged()).getContent());
        model.addAttribute("issueTypes", issueTypeService.findAllParents(Pageable.unpaged()).getContent());
        model.addAttribute("subTypes", issueTypeService.findAllSubTypes(Pageable.unpaged()).getContent());
        model.addAttribute("priorities", IssuePriority.values());
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("userTypes", UserType.values());
    }
}