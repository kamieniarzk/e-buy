package com.example.onlinestore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private double mass;
    private double price;
    private int quantity;
    private String category;
    private String imageUrl;
    private String offeredBy;
    private boolean availableForSale;
    private boolean archived; // if a product is deleted (unavailable for sale) but kept in database for OrderDetails


    public Product(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.description = product.description;
        this.mass = product.mass;
        this.price = product.price;
        this.quantity = product.quantity;
        this.category = product.category;
        this.imageUrl = product.imageUrl;
        this.offeredBy = product.offeredBy;
        availableForSale = true;
        archived = false;
    }
}
