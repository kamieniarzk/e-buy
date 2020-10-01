package com.example.ebuy.service;

import com.example.ebuy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Optional<Product> get(long id);
    Page<Product> getCategory(String category, Pageable pageable);
    Page<Product> getUserProducts(String username, Pageable pageable);
    Page<Product> getProducts(String search, Pageable pageable);
    void save(Product product, boolean edit);
    void deleteById(long id);
    Page<Product> updateAvailableForCart(Page<Product> products);
//    boolean isUserAllowed(long id);

}
