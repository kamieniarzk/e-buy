package com.example.onlinestore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    }
}
