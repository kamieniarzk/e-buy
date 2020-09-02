package com.example.onlinestore.repository;

import com.example.onlinestore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM product WHERE category=:category", nativeQuery = true)
    List<Product> getCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM product WHERE offered_by=:username", nativeQuery = true)
    List<Product> getUserProducts(@Param("username") String username);

    @Query(value = "SELECT * FROM product WHERE name like %:search%", nativeQuery = true)
    Page<Product> getProducts(@Param("search") String search, Pageable pageable);

}
