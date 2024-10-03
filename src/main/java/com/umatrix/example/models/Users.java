package com.umatrix.example.models;


import com.umatrix.example.Base.BaseEntity;
import com.umatrix.example.statics.UserRole;
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
public class Users extends BaseEntity {
    @NotBlank(message = "username must not be blank, null, or empty")
    private String username;
    @NotBlank(message = "password must not be blank, null, or empty")
    private String password;
    @Min(value = 0, message = "the value must not be negative")
    private double balance;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
