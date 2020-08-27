package com.example.onlinestore.controllers;

import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.ImageUploader;
import com.example.onlinestore.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductService productService;
    private final ImageUploader imageUploader;
    private final CartService cartService;

    public CartController(ProductService productService, ImageUploader imageUploader, CartService cartService) {
        this.productService = productService;
        this.imageUploader = imageUploader;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        cartService.checkout();
        return "redirect:/products/home";
    }

    public String increaseQty(Model model, @PathVariable("id") long id) {
        model.addAttribute("cart", cartService.addProduct(id));
        return "redirect:/products/home";
    }


}
