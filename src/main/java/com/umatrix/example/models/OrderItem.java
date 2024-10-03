package com.umatrix.example.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "id must be positive number")
    private Long id;
    @Min(value = 1, message = "quantity must be positive number")
    private Integer quantity;
    @Min(value = 1, message = "total price must be positive number")
    private double totalPrice;
    @ManyToOne
    private Food food;
    @ManyToOne
    private Order order;
}
