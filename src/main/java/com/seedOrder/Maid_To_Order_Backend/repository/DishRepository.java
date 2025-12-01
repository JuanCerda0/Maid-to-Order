package com.seedOrder.Maid_To_Order_Backend.repository;

import com.seedOrder.Maid_To_Order_Backend.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCategoryIgnoreCase(String category);
}