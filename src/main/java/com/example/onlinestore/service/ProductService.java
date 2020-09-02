package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ProductService(ProductRepository productRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public Optional<Product> get(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return updateAvailableForCart(productRepository.findAll());
    }

    public List<Product> getCategory(String category) {
        return updateAvailableForCart(productRepository.getCategory(category));
    }

    public List<Product> getUserProducts(String username) {
        return updateAvailableForCart(productRepository.getUserProducts(username));
    }

    public void save(Product product) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        product.setAvailable(true);
        product.setAvailableForCart(true);
        if(product.getImageUrl() == null) {
            product.setImageUrl("https://image.shutterstock.com/image-vector/picture-vector-icon-no-image-600w-1350441335.jpg");
        }
        productRepository.save(product);
    }

    public void edit(Product product, long id) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        product.setId(id);
        product.setAvailable(true);
        productRepository.save(product);
    }

    public void deleteById(long id) {
        productRepository.findById(id).ifPresent(
                product -> {
                    if(product.isArchived()) {
                        product.setAvailable(false);
                    } else {
                        productRepository.deleteById(id);
                    }
                }
        );
    }

    public List<Product> updateAvailableForCart(List<Product> products) {
        return products.stream()
                .peek(product -> {
                    if(cartService.getCart().getProducts().containsKey(product.getId())) {
                        if(product.getQuantity() > cartService.getCart().getProducts().get(product.getId()).getQuantity()) {
                            product.setAvailableForCart(true);
                        } else {
                            product.setAvailableForCart(false);
                        }
                    } else if(product.getQuantity() > 0) {
                        product.setAvailableForCart(true);
                    }
                }).collect(Collectors.toList());
    }

    public Page<Product> getProducts(String search, Pageable pageable) {
        return productRepository.getProducts(search, pageable);
    }


}
