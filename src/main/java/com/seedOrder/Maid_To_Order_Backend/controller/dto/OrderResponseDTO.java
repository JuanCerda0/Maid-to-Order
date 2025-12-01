package com.seedOrder.Maid_To_Order_Backend.controller.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String address;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponseDTO {
        private Long dishId;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}