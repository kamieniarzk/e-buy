package com.example.onlinestore.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String client;

    @CreationTimestamp
    private Timestamp createdDate;

    // TODO - mapping for products
    private Map<Product, Integer> products;
}
