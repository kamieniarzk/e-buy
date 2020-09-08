package com.example.onlinestore.service;

import com.example.onlinestore.utlis.ShoppingCart;

public interface CartService {
    void addProduct(long id);
    boolean removeProduct(long id);
    long checkout();
    ShoppingCart getCart();
}
