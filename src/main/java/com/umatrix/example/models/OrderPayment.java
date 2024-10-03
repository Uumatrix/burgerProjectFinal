package com.umatrix.example.models;

import com.umatrix.example.statics.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@ToString
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean payment;
    private LocalDate paymentDate;
    @Min(value = 0, message = "total price must be positive number")
    private double totalPrice;
    @OneToOne
    private Order order;
}
