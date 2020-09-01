package com.example.onlinestore.utlis;

import com.example.onlinestore.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ShoppingCart {
    private final Map<Long, Product> cart = new LinkedHashMap<>();
    private Double total;

    public ShoppingCart() {
        total = 0.0;
    }
}
