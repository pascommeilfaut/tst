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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/pos")
@RequiredArgsConstructor
public class PosController {

    private final PosService posService;
    private final CityService cityService;

    @GetMapping("/browse")
    public String browse(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                         Model model) {
        model.addAttribute("posPage", posService.findByFilter(null, pageable));
        return "pos/browse";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("posDto", new SavePosDto());
        populateFormAttributes(model);
        return "pos/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("posDto") SavePosDto posDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            return "pos/add";
        }
        posService.create(posDto);
        return "redirect:/pos/browse";
    }

    private void populateFormAttributes(Model model) {
        // Fetch all cities for the dropdown
        model.addAttribute("cities", cityService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("connectionTypes", ConnectionType.values());
        // Days of week 1-7 for the checkboxes
        model.addAttribute("daysOfWeek", IntStream.range(1, 8).boxed().toList());
    }
}