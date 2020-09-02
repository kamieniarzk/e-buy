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
    private boolean available; // if not deleted
    private boolean archived; // if ever sold
    /* when seller/admin attempts to delete a product, a check is made and
    if the product was ever sold then it needs to be persisted in the database
    so that it can be seen from the orders history (only the available boolean is toggled then),
    however if it was never sold, then it can be deleted from the database
     */
    @Transient
    public boolean availableForCart;

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
        this.available = product.available;
        this.archived = product.archived;
        this.availableForCart = product.availableForCart;
    }

    public int incrementQuantity() {
        quantity += 1;
        return quantity;
    }

    public int decrementQuantity() {
        if(quantity > 0 ) {
            quantity -= 1;
        }
        return quantity;
    }
}
