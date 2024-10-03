package com.umatrix.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInDto {
    @NotBlank(message = "username must not be blank, null, or empty")
    private String username;
    @NotBlank(message = "password must not be blank, null, or empty")
    private String password;
}
