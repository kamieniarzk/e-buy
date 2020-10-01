package com.example.ebuy.repository;

import com.example.ebuy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p WHERE lower(p.category)=lower(:category) AND p.available=true")
    Page<Product> getCategory(@Param("category") String category, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.offeredBy=:username AND p.available=true")
    Page<Product> getUserProducts(@Param("username") String username, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE lower(p.name) like lower(concat('%', :search,'%')) AND p.available=true")
    Page<Product> getProducts(@Param("search") String search, Pageable pageable);

}
