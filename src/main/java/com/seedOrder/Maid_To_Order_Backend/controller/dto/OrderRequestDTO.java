package com.seedOrder.Maid_To_Order_Backend.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String customerName;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El teléfono debe tener entre 8 y 15 dígitos")
    private String customerPhone;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;

    @NotNull(message = "La lista de items no puede ser nula")
    @Size(min = 1, message = "Debe incluir al menos un item en la orden")
    @Valid
    private List<OrderItemDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {
        
        @NotNull(message = "El ID del plato es obligatorio")
        @Positive(message = "El ID del plato debe ser un número positivo")
        private Long dishId;
        
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Max(value = 100, message = "La cantidad no puede exceder 100 unidades")
        private Integer quantity;
    }
}