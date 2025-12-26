package com.iongroup.controllers;

import com.iongroup.data.user.UserType;
import com.iongroup.service.UserService;
import com.iongroup.service.dto.SaveUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN") // Equivalent to @RolesAllowed("ADMIN")
public class UserController {

    private final UserService userService;

    @GetMapping("/browse")
    public String browse(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                         Model model) {
        model.addAttribute("users", userService.findByFilter(null, pageable));
        return "users/browse";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("userDto", new SaveUserDto());
        model.addAttribute("userTypes", UserType.values());
        return "users/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("userDto") SaveUserDto userDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userTypes", UserType.values());
            return "users/add";
        }

        try {
            userService.create(userDto);
        } catch (IllegalArgumentException e) {
            // Handle specific business errors (like duplicate login)
            bindingResult.rejectValue("login", "error.login", e.getMessage());
            model.addAttribute("userTypes", UserType.values());
            return "users/add";
        }

        return "redirect:/users/browse";
    }
}