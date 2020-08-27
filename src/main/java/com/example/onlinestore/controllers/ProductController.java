package com.example.onlinestore.controllers;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.ProductCategory;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.ImageUploader;
import com.example.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ImageUploader imageUploader;
    private final CartService cartService;

    @Autowired
    public ProductController(ImageUploader imageUploader, CartService cartService, ProductService productService) {
        this.imageUploader = imageUploader;
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/category/{category}")
    public String showCategory(@PathVariable("category") String category,  Model model) {
        model.addAttribute("products", productService.getCategory(category));
        model.addAttribute("categories", ProductCategory.getCategories());
        model.addAttribute("cart", cartService.getCart());
        return "home";
    }

    @GetMapping("/myProducts")
    public String showMyProducts(Model model) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("products", productService.getUserProducts(loggedUser.getName()));
        model.addAttribute("cart", cartService.getCart());
        return "home";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") long id, Model model) {
        productService.get(id).ifPresent(product -> model.addAttribute("product", product));
        model.addAttribute("categories", ProductCategory.getCategories());
        model.addAttribute("cart", cartService.getCart());
        return "product";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        model.addAttribute("categories", ProductCategory.getCategories());
        model.addAttribute("product", product);
        return "add-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile file, Model model) {
        if(!file.isEmpty()) {
            product.setImageUrl(imageUploader.upload(file));
        }
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        productService.save(product);

        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("categories", ProductCategory.getCategories());
        return "redirect:/products/home";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        productService.deleteById(id);
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("categories", ProductCategory.getCategories());
        return "redirect:/products/home";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") long id, Model model) {
        model.addAttribute("cart", cartService.addProduct(id));
        model.addAttribute("categories", ProductCategory.getCategories());
        return "redirect:/products/home";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable("id") long id, Model model) {
        model.addAttribute("cart", cartService.removeProduct(id));
        model.addAttribute("categories", ProductCategory.getCategories());
        return "redirect:/products/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("products", productService.getAll());
        model.addAttribute("categories", ProductCategory.getCategories());
        return "home";
    }
}
