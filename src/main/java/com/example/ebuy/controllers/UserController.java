package com.example.ebuy.controllers;
import com.example.ebuy.model.User;
import com.example.ebuy.model.Product;
import com.example.ebuy.service.impl.CartServiceImpl;
import com.example.ebuy.service.impl.ImageUploadServiceImpl;
import com.example.ebuy.service.impl.ProductServiceImpl;
import com.example.ebuy.service.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final ImageUploadServiceImpl imageUploadServiceImpl;


    public UserController(UserServiceImpl userServiceImpl, ProductServiceImpl productServiceImpl, CartServiceImpl cartServiceImpl, ImageUploadServiceImpl imageUploadServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.productServiceImpl = productServiceImpl;
        this.cartServiceImpl = cartServiceImpl;
        this.imageUploadServiceImpl = imageUploadServiceImpl;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("edit", false);
        return "user-form";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/{username}")
    public String get(Model model, @PathVariable("username") String username, @RequestParam Optional<Integer> page,
                      @RequestParam Optional<String> sortBy) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", loggedUser.getName());
        Page<Product> products = productServiceImpl.getUserProducts(username,
                PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("name")));
        int totalPages = products.getTotalPages();
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("cart", cartServiceImpl.getCart());
        model.addAttribute("user", userServiceImpl.loadUserByUsername(username));
        return "user";
    }

    @GetMapping("/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        model.addAttribute("edit", true);
        model.addAttribute("user", userServiceImpl.loadUserByUsername(username));
        return "user-form";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile file,
                         @RequestParam("edit") boolean edit) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("edit", edit);
            return "user-form";
        } else {
            if(!file.isEmpty()) {
                user.setImageUrl(imageUploadServiceImpl.upload(file));
            }
            String message = userServiceImpl.save(user);
            if(message == null) {
                if(edit) {
                    return "redirect:/users/" + user.getUsername();
                } else {
                    return "login";
                }
            } else {
                model.addAttribute("edit", edit);
                model.addAttribute("message", message);
                return "user-form";
            }
        }
    }
}
