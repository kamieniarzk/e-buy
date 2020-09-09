package com.example.onlinestore.controllers;

import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.service.impl.CartServiceImpl;
import com.example.onlinestore.service.impl.OrderDetailsServiceImpl;
import com.example.onlinestore.service.impl.ProductServiceImpl;

import com.example.onlinestore.utlis.ShoppingCart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@SessionAttributes({"cart"})
public class CartController {
    private final CartServiceImpl cartServiceImpl;
    private final OrderDetailsServiceImpl orderDetailsServiceImpl;

    @ModelAttribute("cart")
    public ShoppingCart cart() {
        return cartServiceImpl.getCart();
    }

    public CartController(CartServiceImpl cartServiceImpl, OrderDetailsServiceImpl orderDetailsServiceImpl) {
        this.cartServiceImpl = cartServiceImpl;
        this.orderDetailsServiceImpl = orderDetailsServiceImpl;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkout")
    public String checkout(Model model, RedirectAttributes redirectAttributes) {
        long id = cartServiceImpl.checkout();
        if(id == -1) {
            redirectAttributes.addFlashAttribute("message", "Something went wrong with checkout. Please try again.");
            return "redirect:/products/home";
        } else {
            OrderDetails orderDetails = orderDetailsServiceImpl.get(id);
            model.addAttribute("products", orderDetailsServiceImpl.populateProducts(orderDetails.getProducts()));
            model.addAttribute("order", orderDetails);
            return "order";
        }
    }



}
