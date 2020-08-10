package com.example.personalblog.controllers;

import com.example.personalblog.entities.User;
import com.example.personalblog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }
    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String password, @RequestParam String email, Model model){
        userService.updateProfile(user, password, email);
        return "redirect:/profile";
    }
}
