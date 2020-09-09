package com.example.onlinestore.service;

import com.example.onlinestore.model.OrderDetails;
import com.example.onlinestore.model.Product;

import java.util.List;
import java.util.Map;

public interface OrderDetailsService {
    OrderDetails get(long id);
    List<OrderDetails> getUserOrders(String username);
    long save(List<Product> products);
    List<Product> populateProducts(Map<Long, Integer> cart);
}
