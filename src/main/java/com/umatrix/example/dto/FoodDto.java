package com.umatrix.example.dto;

import com.umatrix.example.Base.BaseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class FoodDto extends BaseDto {
    @NotBlank(message = "description must not be blank, null or empty")
    private String description;
    @Min(value = 0, message = "price must be positive number")
    private double price;
    @Min(value = 0, message = "quantity must be positive number")
    private int quantity;
    @Min(value = 0, message = "weight must be positive number")
    private double weight;
    @NotBlank(message = "composition must not be blank, null or empty")
    private String composition;
    @Min(value = 0, message = "category id must be positive number")
    private Long categoryId;
}
