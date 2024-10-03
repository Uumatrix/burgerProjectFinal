package com.umatrix.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;



@Getter
@Setter
public class OrderDto {
    @NotBlank(message = "address must not be blank, null, or empty")
    private String address;
    @Min(value = 0, message = "user id must be positive number")
    private Long userId;
}


