package com.example.ebuy.service;

import com.example.ebuy.utlis.ShoppingCart;

public interface CartService {
    void addProduct(long id);
    boolean removeProduct(long id);
    long checkout();
    ShoppingCart getCart();
}
