package com.seedOrder.Maid_To_Order_Backend.service;

import com.seedOrder.Maid_To_Order_Backend.controller.OrderController;
import com.seedOrder.Maid_To_Order_Backend.controller.dto.OrderResponseDTO;
import com.seedOrder.Maid_To_Order_Backend.controller.dto.OrderRequestDTO;
import com.seedOrder.Maid_To_Order_Backend.model.Dish;
import com.seedOrder.Maid_To_Order_Backend.model.OrderEntity;
import com.seedOrder.Maid_To_Order_Backend.model.OrderItem;
import com.seedOrder.Maid_To_Order_Backend.repository.DishRepository;
import com.seedOrder.Maid_To_Order_Backend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final DishRepository dishRepo;

    public OrderService(OrderRepository orderRepo, DishRepository dishRepo) {
        this.orderRepo = orderRepo;
        this.dishRepo = dishRepo;
    }

    public OrderEntity saveFromDto(OrderRequestDTO dto) {
        OrderEntity order = new OrderEntity();
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setAddress(dto.getAddress());

        // build items, calculate totals
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequestDTO.OrderItemDTO it : dto.getItems()) {
            Dish dish = dishRepo.findById(it.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + it.getDishId()));

            OrderItem item = OrderItem.builder()
                    .dishId(dish.getId())
                    .name(dish.getName())
                    .price(dish.getPrice())
                    .quantity(it.getQuantity())
                    .subtotal(dish.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                    .order(order)
                    .build();

            order.getItems().add(item);
            total = total.add(item.getSubtotal());
        }

        order.setTotal(total);
        order.setStatus("PENDING");

        return orderRepo.save(order);
    }

    public OrderEntity findById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public java.util.List<OrderEntity> findAll() {
        return orderRepo.findAll();
    }

    public OrderEntity updateStatus(Long id, String status) {
        return orderRepo.findById(id).map(o -> {
            o.setStatus(status);
            return orderRepo.save(o);
        }).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public void delete(Long id) {
        orderRepo.deleteById(id);
    }

    // Mapper to DTO (simple)
    public OrderResponseDTO toDto(OrderEntity order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .address(order.getAddress())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .total(order.getTotal())
                .items(order.getItems().stream()
                        .map(it -> OrderResponseDTO.OrderItemResponseDTO.builder()
                                .dishId(it.getDishId())
                                .name(it.getName())
                                .price(it.getPrice())
                                .quantity(it.getQuantity())
                                .subtotal(it.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}