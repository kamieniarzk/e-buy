package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CartService cartService;


    public Optional<Product> get(long id) {
        if(cartService.getStock().containsKey(id)) {
            return Optional.of(cartService.getStock().get(id));
        } else {
            return Optional.empty();
        }
    }

    public ProductService(ProductRepository productRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public List<Product> getAll() {
        return cartService.getStock().values().stream()
                .collect(Collectors.toList());
    }

    public List<Product> getCategory(String category) {
        return cartService.getStock().values().stream()
                .filter(product -> product.getCategory().equals(category.toUpperCase()))
                .collect(Collectors.toList());
    }

    public List<Product> getUserProducts(String username) {
        return cartService.getStock().values().stream()
                .filter(product -> product.getOfferedBy().equals(username))
                .collect(Collectors.toList());
    }

    public void save(Product product) {
        productRepository.save(product);
        cartService.addToStock(product);
    }

    public void deleteById(long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            cartService.deleteFromStock(id);
        }
    }


}
