package com.example.onlinestore.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String client;
    private double total;
    @CreationTimestamp
    private Timestamp date;

    // TODO - mapping for products

    @ElementCollection
    private Map<Long, Integer> products = new LinkedHashMap<>();
}
