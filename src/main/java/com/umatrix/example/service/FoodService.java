package com.umatrix.example.service;


import com.umatrix.example.exceptionHandling.CustomExceptions.FoodNotFound;
import com.umatrix.example.models.Food;
import com.umatrix.example.repository.FoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {


    private final FoodRepo foodRepo;

    @Autowired
    public FoodService(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }


    public Food create(Food food) {
        return foodRepo.save(food);
    }

    public List<Food> findAll() {
        return foodRepo.findAll();
    }

    public Food findById(Long id) {
        return foodRepo.findById(id)
                .orElseThrow(FoodNotFound::new);
    }

    public void delete(Long id) {
        foodRepo.deleteById(id);
    }

    public boolean checkExistence(String foodName) {
        return foodRepo.existsByName(foodName);
    }

    public void deleteAllFoodsByCategory(Long categoryId) {
        foodRepo.deleteAllByCategoryId(categoryId);
    }
}

