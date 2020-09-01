package com.example.onlinestore.controllers;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/orders")
@SessionAttributes({"cart"})
public class OrderController {
    private final OrderDetailsService orderDetailsService;
    private final CartService cartService;

    public OrderController(OrderDetailsService orderDetailsService, CartService cartService) {
        this.orderDetailsService = orderDetailsService;
        this.cartService = cartService;
    }

    @ModelAttribute("cart")
    public Map<Long, Product> cart() {
        return cartService.getCart();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderDetailsService.getAll());
        return "order-list";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") long id, Model model) {
        model.addAttribute("order", orderDetailsService.get(id));
        return "order";
    }
}
