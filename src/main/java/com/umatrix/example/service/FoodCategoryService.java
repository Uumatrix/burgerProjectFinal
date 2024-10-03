package com.umatrix.example.service;

import com.umatrix.example.exceptionHandling.CustomExceptions.FoodCategoryNotFound;
import com.umatrix.example.models.FoodCategory;
import com.umatrix.example.repository.FoodCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodCategoryService {

    private final FoodCategoryRepo foodCategoryRepo;

    @Autowired
    public FoodCategoryService(FoodCategoryRepo foodCategoryRepo) {
        this.foodCategoryRepo = foodCategoryRepo;
    }

    public FoodCategory findCategoryById(long id){
        return foodCategoryRepo.findById(id)
                .orElseThrow(FoodCategoryNotFound::new);
    }

    public FoodCategory create(FoodCategory foodCategory){
        return foodCategoryRepo.save(foodCategory);
    }

    public List<FoodCategory> findAll(){
        return foodCategoryRepo.findAll();
    }

    public void deleteById(long id){
        foodCategoryRepo.deleteById(id);
    }

    public boolean checkExistence(String categoryName){
        return foodCategoryRepo.existsByName(categoryName);
    }
}
