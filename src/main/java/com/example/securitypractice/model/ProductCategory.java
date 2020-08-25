package com.example.securitypractice.model;

import java.util.ArrayList;
import java.util.List;

public enum ProductCategory {
    MEAT("meat"), DAIRY("dairy"), SEAFOOD("seafood"), BAKERY("bakery"), SNACKS("snacks"), CANDY("candy"), BEVERAGES("beverages"), DELI("deli");

    private final String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
    public static List<ProductCategory> getCategories() {
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(MEAT);
        categories.add(DAIRY);
        categories.add(SEAFOOD);
        categories.add(BAKERY);
        categories.add(SNACKS);
        categories.add(CANDY);
        categories.add(BEVERAGES);
        categories.add(DELI);
        return categories;
    }
}
