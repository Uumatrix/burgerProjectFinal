package com.umatrix.example.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umatrix.example.Base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@ToString
public class Food extends BaseEntity {
    @NotBlank(message = "description must not be blank, null or empty")
    private String description;
    @Min(value = 0, message = "price must be positive number")
    private double price;
    @Min(value = 0, message = "quantity must be positive number")
    private Integer quantity;
    @Min(value = 0, message = "weight must be positive number")
    private double weight;
    @NotBlank(message = "composition must not be blank, null or empty")
    private String composition;
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BYTEA")
    private byte[] image;
    @ManyToOne
    private FoodCategory category;

}
