package com.example.onlinestore.controllers;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.ProductCategory;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.ImageUploader;
import com.example.onlinestore.service.ProductService;
import com.example.onlinestore.utlis.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/products")
@SessionAttributes({"cart", "categories", "username"})
public class ProductController {
    private final ProductService productService;
    private final ImageUploader imageUploader;
    private final CartService cartService;

    @ModelAttribute("cart")
    public ShoppingCart get() {
        return cartService.getCart();
    }

    @ModelAttribute("username")
    public String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @ModelAttribute("categories")
    public List<ProductCategory> categories() {
        return ProductCategory.getCategories();
    }


    @Autowired
    public ProductController(ImageUploader imageUploader, CartService cartService, ProductService productService) {
        this.imageUploader = imageUploader;
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/category/{category}")
    public String showCategory(@PathVariable("category") String category,  Model model) {
        model.addAttribute("products", productService.getCategory(category));
        return "home";
    }

    @GetMapping("/myProducts")
    public String showMyProducts(Model model) {
        String username = (String) model.getAttribute("username");
        model.addAttribute("products", productService.getUserProducts(username));
        return "home";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") long id, Model model) {
        productService.get(id).ifPresent(product -> model.addAttribute("product", product));
        return "product";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        model.addAttribute("edit", false);
        model.addAttribute("product", product);
        return "add-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile file, Model model) {
        if(!file.isEmpty()) {
            product.setImageUrl(imageUploader.upload(file));
        }
        productService.save(product);
        return "redirect:/products/home";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        model.addAttribute("edit", true);
        model.addAttribute("product", productService.get(id).get());
        return "add-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, @ModelAttribute("product") Product product, Model model,
                         @RequestParam("image") MultipartFile file) {
        if(!file.isEmpty()) {
            product.setImageUrl(imageUploader.upload(file));
        }
        productService.edit(product, id);
        return "redirect:/products/home";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        productService.deleteById(id);
        return "redirect:/products/home";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") long id, Model model, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        cartService.addProduct(id);
        return "redirect:" + referrer;
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable("id") long id, Model model) {
        cartService.removeProduct(id);
        return "redirect:/products/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.getAll());
        return "home";
    }
}
