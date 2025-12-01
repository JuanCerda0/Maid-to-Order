package com.seedOrder.Maid_To_Order_Backend.service;

import com.seedOrder.Maid_To_Order_Backend.model.Dish;
import com.seedOrder.Maid_To_Order_Backend.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishService {

    private final DishRepository repo;

    public DishService(DishRepository repo) {
        this.repo = repo;
    }

    public List<Dish> findAll() {
        return repo.findAll();
    }

    public List<Dish> findByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    public Dish findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Dish not found: " + id));
    }

    public Dish save(Dish d) {
        // add business validations as needed
        return repo.save(d);
    }

    public Dish update(Long id, Dish newDish) {
        return repo.findById(id).map(d -> {
            d.setName(newDish.getName());
            d.setDescription(newDish.getDescription());
            d.setPrice(newDish.getPrice());
            d.setCategory(newDish.getCategory());
            d.setImageUrl(newDish.getImageUrl());
            d.setAvailable(newDish.isAvailable());
            return repo.save(d);
        }).orElseThrow(() -> new RuntimeException("Dish not found: " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}