package com.example.securitypractice.service;

import com.example.securitypractice.model.Product;
import com.example.securitypractice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getCategory(String category) {
        return productRepository.getCategory(category.toUpperCase());
    }

    public List<Product> getUserProducts(String username) {
        return productRepository.getUserProducts(username);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

}
