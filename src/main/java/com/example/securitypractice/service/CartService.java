package com.example.securitypractice.service;

import com.example.securitypractice.model.Product;
import com.example.securitypractice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@SessionScope
@Transactional
public class CartService {
    private final Map<String, Integer> cart = new LinkedHashMap<>();
    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    public Map<String, Integer> addProduct(long id) {
        if(productRepository.existsById(id)) {
            Product product = productRepository.getOne(id);
            if(product.getQuantity() > 0) {
                if(cart.containsKey(product.getName())) {
                    cart.replace(product.getName(), cart.get(product.getName()) + 1);
                } else {
                    cart.put(product.getName(), 1);
                }
                product.setQuantity(product.getQuantity() - 1);
                productRepository.save(product);
            }
        }
        return cart;
    }

    public Map<String, Integer> getCart() {
        return cart;
    }
}
