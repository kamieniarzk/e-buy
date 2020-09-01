package com.example.onlinestore.service;

import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.OrderDetailsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderDetailsService {
    private final CartService cartService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductService productService;

    public OrderDetailsService(CartService cartService, OrderDetailsRepository orderDetailsRepository, ProductService productService) {
        this.cartService = cartService;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productService = productService;
    }

    public List<OrderDetails> getAll() {
        return orderDetailsRepository.findAll();
    }

    public long save(List<Product> products) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setClient(SecurityContextHolder.getContext().getAuthentication().getName());
        products.forEach(product -> {
            orderDetails.setTotal(orderDetails.getTotal() + product.getPrice() * product.getQuantity());
            orderDetails.getProducts().put(product.getId(), product.getQuantity());
        });
        return orderDetailsRepository.save(orderDetails).getId();
    }

    public OrderDetails get(long id) {
        return orderDetailsRepository.findById(id).get();
    }

    public List<Product> populateProducts(Map<Long, Integer> cart) {
        List<Product> products = new ArrayList<>();
        cart.keySet().forEach(
                id -> {
                    Product product = new Product(productService.get(id).get());
                    product.setQuantity(cart.get(id));
                    products.add(product);
                }
        );
        return products;
    }
}
