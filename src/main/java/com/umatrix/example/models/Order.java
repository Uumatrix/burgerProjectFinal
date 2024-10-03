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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "id must be positive number")
    private Long id;
    private LocalDate orderDate;
    @NotBlank(message = "address must not be blank, null, or empty")
    private String address;
    @ManyToOne
    private Users user;
}
