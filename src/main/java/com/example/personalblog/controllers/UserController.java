package com.example.personalblog.controllers;

import com.example.personalblog.entities.Role;
import com.example.personalblog.entities.User;
import com.example.personalblog.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user-list")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "user-list";
    }

    @GetMapping("/{user}")
    public String userEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getRoles());
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }
    @PostMapping("/user-manager")
    public String userSave(
            @RequestParam("userId") User user,
            @RequestParam("userRole") String[] updatedRoles,
            @RequestParam String userName) {
        user.setUsername(userName);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (String singleRole : updatedRoles) {
            if (roles.contains(singleRole)) {
                user.getRoles().add(Role.valueOf(singleRole));
            }
        }

        userRepo.save(user);

        return "redirect:/user-list";
    }
}
