package com.example.onlinestore.controllers;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.ProductCategory;
import com.example.onlinestore.security.UserRole;
import com.example.onlinestore.service.impl.CartServiceImpl;
import com.example.onlinestore.service.impl.ImageUploadServiceImpl;
import com.example.onlinestore.service.impl.ProductServiceImpl;
import com.example.onlinestore.utlis.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/products")
@SessionAttributes({"cart", "categories", "username"})
public class ProductController {
    private final ProductServiceImpl productServiceImpl;
    private final ImageUploadServiceImpl imageUploadServiceImpl;
    private final CartServiceImpl cartServiceImpl;

    @ModelAttribute("cart")
    public ShoppingCart get() {
        return cartServiceImpl.getCart();
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
    public ProductController(ImageUploadServiceImpl imageUploadServiceImpl, CartServiceImpl cartServiceImpl, ProductServiceImpl productServiceImpl) {
        this.imageUploadServiceImpl = imageUploadServiceImpl;
        this.cartServiceImpl = cartServiceImpl;
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping("/category/{category}")
    public String showCategory(@PathVariable("category") String category, @RequestParam Optional<Integer> page,
                               @RequestParam Optional<String> sortBy, Model model) {
        Page<Product> products = productServiceImpl.getCategory(category,
                PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("name")));
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("products", products);
        model.addAttribute("currentCategory", category);
        return "home";
    }

    @PreAuthorize("hasAuthority('product:write')")
    @GetMapping("/myProducts")
    public String showMyProducts(Model model, @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
        String username = (String) model.getAttribute("username");
        Page<Product> products = productServiceImpl.getUserProducts(username,
                PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("name")));
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") long id, Model model) {
        productServiceImpl.get(id).ifPresent(product -> model.addAttribute("product", productServiceImpl.updateAvailableForCart(product)));
        return "product";
    }

    @PreAuthorize("hasAuthority('product:write')")
    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        model.addAttribute("edit", false);
        model.addAttribute("product", product);
        return "product-form";
    }

    @PreAuthorize("hasAuthority('product:write')")
    @PostMapping("/add")
    public String add(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                      @RequestParam("image") MultipartFile file, @RequestParam("edit") boolean edit, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("edit", edit);
            model.addAttribute("product", product);
            return "product-form";
        } else {
            if(!file.isEmpty()) {
                product.setImageUrl(imageUploadServiceImpl.upload(file));
            }
            productServiceImpl.save(product, edit);
            return "redirect:/products/home";
        }

    }

    @PreAuthorize("hasAuthority('product:write')")
    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        if(productServiceImpl.isUserAllowed(id)) {
            model.addAttribute("edit", true);
            model.addAttribute("product", productServiceImpl.get(id).get());
            return "product-form";
        } else {
            model.addAttribute("message", "Acces denied.");
            return "redirect:/products/home";
        }
    }


    @PreAuthorize("hasAuthority('product:write')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        if(productServiceImpl.isUserAllowed(id)) {
            productServiceImpl.deleteById(id);
        } else {
            model.addAttribute("message", "Acces denied.");
        }
        return "redirect:/products/home";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") long id,
                            @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        cartServiceImpl.addProduct(id);
        return "redirect:" + referrer;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(@PathVariable("id") long id,
                                 @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        cartServiceImpl.removeProduct(id);
        return "redirect:" + referrer;
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam Optional<String> search,
                       @RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
        Page<Product> products = productServiceImpl.getProducts(search.orElse("_"),
                PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("name")));
        int totalPages = products.getTotalPages();
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", products);
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "home";
    }


}
