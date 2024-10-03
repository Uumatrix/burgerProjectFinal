package com.umatrix.example.models;

import com.umatrix.example.Base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
public class FoodCategory extends BaseEntity {
}
