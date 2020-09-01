package com.example.onlinestore.controllers;

import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.ImageUploader;
import com.example.onlinestore.service.OrderDetailsService;
import com.example.onlinestore.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
@SessionAttributes({"cart"})
public class CartController {
    private final ProductService productService;
    private final CartService cartService;
    private final OrderDetailsService orderDetailsService;

    @ModelAttribute("cart")
    public Map<Long, Product> cart() {
        return cartService.getCart();
    }
    public CartController(ProductService productService, CartService cartService, OrderDetailsService orderDetailsService) {
        this.productService = productService;
        this.cartService = cartService;
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        long id = cartService.checkout();
        if(id == -1) {
            model.addAttribute("message", "could not checkout");
            return "redirect:/products/home";
        } else {
            OrderDetails orderDetails = orderDetailsService.get(id);
            model.addAttribute("products", orderDetailsService.populateProducts(orderDetails.getProducts()));
            model.addAttribute("order", orderDetails);
            return "order";
        }
    }

    public String increaseQty(Model model, @PathVariable("id") long id) {
        cartService.addProduct(id);
        return "redirect:/products/home";
    }


}
