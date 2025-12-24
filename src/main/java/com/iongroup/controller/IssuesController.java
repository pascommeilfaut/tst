package com.iongroup.controller;

import com.iongroup.service.dto.CreateIssueDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/issues")
public class IssuesController {

    @GetMapping("/browse")
    public String browse(Model model) {
        return "issues/browse";
    }

    @PostMapping("/add")
    public String addIssue(Model model, @ModelAttribute @Valid CreateIssueDto dto) {
        return "issues/add";
    }

    @PostMapping("/details")
    public String details(Model model, @ModelAttribute @Valid CreateIssueDto dto) {
        return "issues/details";
    }

}