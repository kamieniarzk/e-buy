package com.example.onlinestore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min = 2, max = 20)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private @DecimalMin("0") @DecimalMax("1000.0") BigDecimal mass;
    @NotNull
    private @DecimalMin("0.01") BigDecimal price;
    @Min(1)
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

    public Product(Product product, boolean edit) {
        if(!edit) {
            this.id = product.id;
        }
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
