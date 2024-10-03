package com.umatrix.example.Base;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDto {
    @NotBlank(message = "name must not be blank, null or empty")
    private String name;
}
