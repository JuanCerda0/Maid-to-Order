package com.seedOrder.Maid_To_Order_Backend.repository;

import com.seedOrder.Maid_To_Order_Backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}