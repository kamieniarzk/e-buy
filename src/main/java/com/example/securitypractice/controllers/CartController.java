package com.example.securitypractice.controllers;

import com.example.securitypractice.service.CartService;
import com.example.securitypractice.service.ImageUploader;
import com.example.securitypractice.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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

    public String increaseQty(Model model, @PathVariable("id") long id) {
        model.addAttribute("cart", cartService.addProduct(id));
        return "redirect:/products/home";
    }


}
