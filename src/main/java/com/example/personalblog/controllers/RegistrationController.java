package com.example.personalblog.controllers;

import com.example.personalblog.entities.User;
import com.example.personalblog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){

        if(!userService.addUser(user)){
            model.addAttribute("userExists", "Такий користувач вже існує");
            return "registration";
        }
        model.addAttribute("serviceMessage", "На Вашу пошту було надіслано посилання, перейдіть за вказаною адресою для активації облікового запису");

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if(isActivated) {
            model.addAttribute("activationSuccessful", "Активація пройшла успішно");
        } else {
            model.addAttribute("activationFailed", "Код активації недійсний");
        }
        return "login";
    }
}
