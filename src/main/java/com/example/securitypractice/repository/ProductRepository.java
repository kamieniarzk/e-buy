package com.example.securitypractice.repository;

import com.example.securitypractice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM product WHERE category=:category", nativeQuery = true)
    List<Product> getCategory(@Param("category") String category);
}
