package com.iongroup.controller;

import com.iongroup.service.dto.SavePosDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pos")
public class PosController {

    @GetMapping("/browse")
    public String browse(Model model) {
        return "pos/browse";
    }

    @PostMapping("/add")
    public String addIssue(Model model, @ModelAttribute @Valid SavePosDto dto) {
        return "pos/add";
    }

    @PostMapping("/details")
    public String details(Model model, @ModelAttribute @Valid SavePosDto dto) {
        return "pos/details";
    }

}
