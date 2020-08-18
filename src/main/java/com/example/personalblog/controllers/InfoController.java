package com.example.personalblog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("disclaimer")
    public String disc(){
        return "disclaimer";
    }
    @GetMapping("privacy")
    public String privacy(){
        return "privacy-policy";
    }
    @GetMapping("terms")
    public String terms(){
        return "terms-of-use";
    }
    @GetMapping("about")
    public String about(){
        return "about";
    }
}
