package com.example.ebuy.repository;

import com.example.ebuy.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "SELECT * FROM order_details WHERE client=:username", nativeQuery = true)
    public List<OrderDetails> getUserOrders(@Param("username") String username);
}
