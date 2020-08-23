package com.example.securitypractice.model;

public enum ProductCategory {
    MEAT("meat"), DAIRY("dairy"), SEAFOOD("seafood"), BAKERY("bakery"), SNACKS("snacks"), CANDY("candy"), BEVERAGES("beverages"), DELI("deli");

    private final String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
