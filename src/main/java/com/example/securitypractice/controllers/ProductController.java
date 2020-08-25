package com.example.securitypractice.controllers;

import com.example.securitypractice.model.Product;
import com.example.securitypractice.model.ProductCategory;
import com.example.securitypractice.repository.ProductRepository;
import com.example.securitypractice.service.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ImageUploader imageUploader;

    public ProductController(ProductRepository productRepository, ImageUploader imageUploader) {
        this.productRepository = productRepository;
        this.imageUploader = imageUploader;
    }

    @GetMapping("/{category}")
    public String showCategory(@PathVariable("category") String category,  Model model) {
        String cat = category.toUpperCase();
        System.out.println(cat);
        List<Product> products = productRepository.getCategory(cat);
        System.out.println(products);
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        model.addAttribute("categories", ProductCategory.getCategories());
        model.addAttribute("product", product);
        return "add-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("customer") Product product, @RequestParam("image") MultipartFile file) {
        product.setImageUrl(imageUploader.upload(file));
        productRepository.save(product);

        return "redirect:/home";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
        return "redirect:/home";
    }
}
