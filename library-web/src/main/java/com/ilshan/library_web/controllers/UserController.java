package com.ilshan.library_web.controllers;

import com.ilshan.library_web.models.Role;
import com.ilshan.library_web.models.User;
import com.ilshan.library_web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/registration")
    public String registrationPage(
            @ModelAttribute("user") User user,
            Model model) {
        model.addAttribute("roles", Role.values());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/login";
    }
}
