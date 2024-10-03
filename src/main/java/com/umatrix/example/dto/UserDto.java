package com.umatrix.example.dto;

import com.umatrix.example.Base.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class UserDto extends BaseDto {
    @NotBlank(message = "username must not be blank, null, or empty")
    private String username;
    @NotBlank(message = "password must not be blank, null, or empty")
    private String password;

}
