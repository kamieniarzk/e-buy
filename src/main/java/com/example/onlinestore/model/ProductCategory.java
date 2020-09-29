package com.example.onlinestore.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public enum ProductCategory {
    MEAT("meat"), DAIRY("dairy"), SEAFOOD("seafood"), BAKERY("bakery"), SNACKS("snacks"), CANDY("candy"), BEVERAGES("beverages"), DELI("deli"), FRUIT("fruit"), VEGETABLES("vegetables");

    private final String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
    public static List<ProductCategory> getCategories() {
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(BAKERY);
        categories.add(BEVERAGES);
        categories.add(CANDY);
        categories.add(DAIRY);
        categories.add(DELI);
        categories.add(FRUIT);
        categories.add(MEAT);
        categories.add(SEAFOOD);
        categories.add(SNACKS);
        categories.add(VEGETABLES);
        return categories;
    }
}
