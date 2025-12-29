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
    public String addForm(Model model) {
        model.addAttribute("posDto", new SavePosDto());
        model.addAttribute("formAction", "/pos/save");
        populateFormAttributes(model);
        return "pos/add";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        SavePosDto dto = posService.findSavePosDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("POS not found: " + id));
        model.addAttribute("posDto", dto);
        model.addAttribute("formAction", "/pos/update/" + id);
        populateFormAttributes(model);
        return "pos/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("posDto") SavePosDto posDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/pos/save");
            populateFormAttributes(model);
            return "pos/add";
        }
        posService.create(posDto);
        return "redirect:/pos/browse";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("posDto") SavePosDto posDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/pos/update/" + id);
            populateFormAttributes(model);
            return "pos/add";
        }
        posService.update(posDto, id);
        return "redirect:/pos/browse";
    }

    private void populateFormAttributes(Model model) {
        model.addAttribute("cities", cityService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("connectionTypes", ConnectionType.values());
        model.addAttribute("daysOfWeek", IntStream.range(1, 8).boxed().toList());
    }
}