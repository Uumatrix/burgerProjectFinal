package com.umatrix.example.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderItemDto {
    @Min(value = 0, message = "quantity must be positive number")
    private Integer quantity;
    @Min(value = 0, message = "food id must be positive number")
    private Long foodId;
    @Min(value = 0, message = "order id must be positive number")
    private Long orderId;
}
