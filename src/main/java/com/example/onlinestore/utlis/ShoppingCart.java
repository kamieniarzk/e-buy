package com.example.onlinestore.utlis;

import com.example.onlinestore.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ShoppingCart {
    private final Map<Long, Product> products = new LinkedHashMap<>();
    private BigDecimal total;

    public ShoppingCart() {
        total = new BigDecimal("0");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void addProduct(Product stockProduct) {
        if(products.containsKey(stockProduct.getId()) && stockProduct.getQuantity() > products.get(stockProduct.getId()).getQuantity()) {
            products.get(stockProduct.getId()).incrementQuantity();
            total = total.add(stockProduct.getPrice());
        } else if(!products.containsKey(stockProduct.getId()) && stockProduct.getQuantity() > 0) {
            Product cartProduct = new Product(stockProduct, false);
            cartProduct.setQuantity(1);
            products.put(cartProduct.getId(), cartProduct);
            total = total.add(stockProduct.getPrice());
        }
    }

    public void removeProduct(Product stockProduct) {
        if (products.containsKey(stockProduct.getId()) && products.get(stockProduct.getId()).getQuantity() > 0) {
            if(products.get(stockProduct.getId()).decrementQuantity() == 0) {
                products.remove(stockProduct.getId());
            }
            total = total.subtract(stockProduct.getPrice());
        }
    }

}
