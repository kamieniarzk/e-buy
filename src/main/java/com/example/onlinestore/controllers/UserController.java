package com.example.onlinestore.controllers;

import com.example.onlinestore.auth.User;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.ImageUploader;
import com.example.onlinestore.service.ProductService;
import com.example.onlinestore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final ImageUploader imageUploader;

    public UserController(UserService userService, ProductService productService, CartService cartService, ImageUploader imageUploader) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;
        this.imageUploader = imageUploader;
    }

    @GetMapping("/{username}")
    public String get(Model model, @PathVariable("username") String username) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", loggedUser.getName());
        model.addAttribute("products", productService.getUserProducts(username));
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("user", userService.loadUserByUsername(username));
        return "user";
    }

    @GetMapping("/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        model.addAttribute("edit", true);
        model.addAttribute("user", userService.loadUserByUsername(username));
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user, Model model, @RequestParam("image") MultipartFile file) {
        if(!file.isEmpty()) {
            user.setImageUrl(imageUploader.upload(file));
        }
        String message = userService.save(user);
        if(message == null) {
            return "login";
        } else {
            model.addAttribute("message", message);
            return "signup";
        }
    }

}
