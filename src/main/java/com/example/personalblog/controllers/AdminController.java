package com.example.personalblog.controllers;

import com.example.personalblog.entities.Role;
import com.example.personalblog.entities.User;
import com.example.personalblog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-list")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/user-list/{user}")
    public String userEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getRoles());
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }
    @PostMapping("/user-list/user-manager")
    public String userSave(
            @RequestParam("userId") User user,
            @RequestParam("userRole") String[] updatedRoles,
            @RequestParam String userName) {
        userService.saveUser(user, userName, updatedRoles);
        return "redirect:/user-list";
    }

}
