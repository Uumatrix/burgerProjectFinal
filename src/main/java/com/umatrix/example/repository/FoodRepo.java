package com.umatrix.example.repository;

import com.umatrix.example.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepo extends JpaRepository<Food, Long> {

    boolean existsByName(String foodName);

    void deleteAllByCategoryId(long categoryId);

}
