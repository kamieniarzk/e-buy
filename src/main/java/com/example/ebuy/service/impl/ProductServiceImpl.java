package com.example.ebuy.service.impl;

import com.example.ebuy.model.Product;
import com.example.ebuy.repository.ProductRepository;
import com.example.ebuy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CartServiceImpl cartServiceImpl;

    public ProductServiceImpl(ProductRepository productRepository, CartServiceImpl cartServiceImpl) {
        this.productRepository = productRepository;
        this.cartServiceImpl = cartServiceImpl;
    }

    public Optional<Product> get(long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getCategory(String category, Pageable pageable) {
        return updateAvailableForCart(productRepository.getCategory(category, pageable));
    }

    public Page<Product> getUserProducts(String username, Pageable pageable) {
        return updateAvailableForCart(productRepository.getUserProducts(username, pageable));
    }

    public Page<Product> getProducts(String search, Pageable pageable) {
        return updateAvailableForCart(productRepository.getProducts(search, pageable));
    }

    public void save(Product product, boolean edit) {
        if(edit) {
            if(product.isArchived()) {
                deleteById(product.getId());
                // make a copy of product with different id
                product = new Product(product, true);
            }
        }
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        product.setOfferedBy(loggedUser.getName());
        product.setAvailable(true);
        product.setAvailableForCart(true);
        if(product.getMass() == null) {
            product.setMass(BigDecimal.ZERO);
        }
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

    public Page<Product> updateAvailableForCart(Page<Product> products) {
        products.stream().forEach(product -> {
            if(cartServiceImpl.getCart().getProducts().containsKey(product.getId())) {
                product.setAvailableForCart(product.getQuantity() > cartServiceImpl.getCart().getProducts().get(product.getId()).getQuantity());
            } else if(product.getQuantity() > 0) {
                product.setAvailableForCart(true);
            }
        });
        return products;
    }

    public Product updateAvailableForCart(Product product) {
        if(cartServiceImpl.getCart().getProducts().containsKey(product.getId())) {
            product.setAvailableForCart(product.getQuantity() > cartServiceImpl.getCart().getProducts().get(product.getId()).getQuantity());
        } else if(product.getQuantity() > 0) {
            product.setAvailableForCart(true);
        }
        return product;
    }

    public boolean isUserAllowed(long id) {
        Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<Product> product = get(id);
        return product.filter(value -> (loggedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER")) && value.getOfferedBy().equals(loggedUser.getName()))
                || loggedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).isPresent();
    }

}
