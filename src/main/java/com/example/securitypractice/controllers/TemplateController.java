package com.example.securitypractice.controllers;

import com.example.securitypractice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {
    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "home";
    }
}
