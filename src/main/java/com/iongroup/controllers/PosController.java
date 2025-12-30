package com.iongroup.controllers;

import com.iongroup.data.pos.connection.ConnectionType;
import com.iongroup.service.CityService;
import com.iongroup.service.PosService;
import com.iongroup.service.dto.SavePosDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@Controller
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final CityService cityService;

    @GetMapping("/browse")
    public String browse(@RequestParam(required = false) String searchTerm,
                         @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                         Model model) {
        model.addAttribute("posPage", posService.findByFilter(searchTerm, pageable));
        return "pos/browse";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addForm(Model model) {
        model.addAttribute("posDto", new SavePosDto());
        populateFormAttributes(model);
        return "pos/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer id, Model model) {
        try {
            SavePosDto dto = posService.findSavePosDtoById(id)
                    .orElseThrow(() -> new IllegalArgumentException("POS not found: " + id));

            model.addAttribute("posDto", dto);
            populateFormAttributes(model);
            model.addAttribute("formAction", "/pos/update/%s".formatted(id));
            return "pos/add";
        } catch (IllegalArgumentException e) {
            return "redirect:/pos/browse";
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@Valid @ModelAttribute("posDto") SavePosDto posDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            return "pos/add";
        }

        try {
            posService.create(posDto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("globalError", e.getMessage());
            populateFormAttributes(model);
            return "pos/add";
        }

        return "redirect:/pos/browse";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("posDto") SavePosDto posDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            return "pos/add";
        }

        try {
            posService.update(posDto, id);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("globalError", e.getMessage());
            populateFormAttributes(model);
            model.addAttribute("formAction", "/pos/update/%s".formatted(id));
            return "pos/add";
        }

        return "redirect:/pos/browse";
    }

    private void populateFormAttributes(Model model) {
        model.addAttribute("cities", cityService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("connectionTypes", ConnectionType.values());
        model.addAttribute("daysOfWeek", IntStream.range(1, 8).boxed().toList());
    }
}