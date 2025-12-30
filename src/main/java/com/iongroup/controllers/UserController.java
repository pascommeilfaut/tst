package com.iongroup.controllers;

import com.iongroup.data.user.UserType;
import com.iongroup.service.UserService;
import com.iongroup.service.dto.EditUserDto;
import com.iongroup.service.dto.SaveUserDto;
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

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/browse")
    @PreAuthorize("hasRole('ADMIN')")
    public String browse(@RequestParam(required = false) String searchTerm,
                         @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                         Model model) {
        model.addAttribute("users", userService.findByFilter(searchTerm, pageable));
        return "users/browse";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addForm(Model model) {
        model.addAttribute("userDto", new SaveUserDto());
        model.addAttribute("userTypes", UserType.values());
        model.addAttribute("formAction", "/users/create");
        return "users/add";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer id, Model model) {
        EditUserDto dto = userService.findEditUserDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        model.addAttribute("userDto", dto);
        model.addAttribute("userTypes", UserType.values());
        model.addAttribute("formAction", "/users/update");
        return "users/add";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("userDto") SaveUserDto userDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userTypes", UserType.values());
            model.addAttribute("formAction", "/users/create");
            return "users/add";
        }
        try {
            userService.create(userDto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("login", "error.login", e.getMessage());
            model.addAttribute("userTypes", UserType.values());
            model.addAttribute("formAction", "/users/create");
            return "users/add";
        }
        return "redirect:/users/browse";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@Valid @ModelAttribute("userDto") EditUserDto userDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userTypes", UserType.values());
            model.addAttribute("formAction", "/users/update");
            return "users/add";
        }
        try {
            userService.update(userDto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("login", "error.login", e.getMessage());
            model.addAttribute("userTypes", UserType.values());
            model.addAttribute("formAction", "/users/update");
            return "users/add";
        }
        return "redirect:/users/browse";
    }
}