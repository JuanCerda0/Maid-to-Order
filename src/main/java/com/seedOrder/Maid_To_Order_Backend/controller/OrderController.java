package com.seedOrder.Maid_To_Order_Backend.controller;

import com.seedOrder.Maid_To_Order_Backend.model.Dish;
import com.seedOrder.Maid_To_Order_Backend.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dishes")
public class OrderController {

    private final DishService service;

    public OrderController(DishService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish d) {
        Dish saved = service.save(d);
        return ResponseEntity.created(URI.create("/api/dishes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> update(@PathVariable Long id, @Valid @RequestBody Dish d) {
        Dish updated = service.update(id, d);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}