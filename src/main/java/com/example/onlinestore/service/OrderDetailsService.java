package com.example.onlinestore.service;

import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.OrderDetailsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderDetailsService {
    private final CartService cartService;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsService(CartService cartService, OrderDetailsRepository orderDetailsRepository) {
        this.cartService = cartService;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public void save(List<Product> products) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setClient(SecurityContextHolder.getContext().getAuthentication().getName());
        //orderDetails.setProducts(products);
        orderDetailsRepository.save(orderDetails);
    }
}
