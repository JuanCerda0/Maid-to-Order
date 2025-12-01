package com.seedOrder.Maid_To_Order_Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, scale = 2, precision = 12)
    private BigDecimal price;

    private String category;

    /**
     * URL (s3, servidor) o path relativo; no guardamos blobs.
     */
    private String imageUrl;

    private boolean available = true;
}
