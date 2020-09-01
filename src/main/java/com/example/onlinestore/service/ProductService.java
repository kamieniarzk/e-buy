package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CartService cartService;

    public ProductService(ProductRepository productRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public Optional<Product> get(long id) {
        return Optional.of(cartService.getSessionStock().get(id));
    }

    public List<Product> getAll() {
        return new ArrayList<>(cartService.getSessionStock().values());
    }

    public List<Product> getCategory(String category) {
        return cartService.getSessionStock().values().stream()
                .filter(product -> product.getCategory().equals(category.toUpperCase()))
                .collect(Collectors.toList());
    }

    public List<Product> getUserProducts(String username) {
        return cartService.getSessionStock().values().stream()
                .filter(product -> product.getOfferedBy().equals(username))
                .collect(Collectors.toList());
    }

    public void save(Product product) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        product.setAvailable(true);
        productRepository.save(product);
        cartService.addToStock(product);
    }

    public void edit(Product product, long id) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        product.setId(id);
        product.setAvailable(true);
        productRepository.save(product);
        cartService.editInStock(product);
    }

    public void deleteById(long id) {
        productRepository.findById(id).ifPresent(
                product -> {
                    if(product.isArchived()) {
                        product.setAvailable(false);
                    } else {
                        productRepository.deleteById(id);
                    }
                    cartService.deleteFromStock(id);
                }
        );
    }


}
