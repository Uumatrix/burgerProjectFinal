package com.umatrix.example.controllers;

import com.umatrix.example.dto.FoodDto;
import com.umatrix.example.exceptionHandling.messageexception.ErrorResponse;
import com.umatrix.example.mapstruct.FoodMapper;
import com.umatrix.example.models.Food;
import com.umatrix.example.models.FoodCategory;
import com.umatrix.example.service.FoodCategoryService;
import com.umatrix.example.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@SecurityRequirement(name = "bearerAuth")
public class FoodController {


    private final FoodService foodService;


    private final FoodCategoryService foodCategoryService;

    @Autowired
    public FoodController(FoodService foodService, FoodCategoryService foodCategoryService) {
        this.foodService = foodService;
        this.foodCategoryService = foodCategoryService;
    }

    @Operation(summary = "create a food", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "food retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "food not found")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createFood(@Valid @RequestBody FoodDto foodDto) {
        Food food = FoodMapper.INSTANCE.toFood(foodDto);
        if (foodService.checkExistence(food.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("this food already exists"));
        }
        FoodCategory foodCategory = foodCategoryService.findCategoryById(foodDto.getCategoryId());
        food.setCategory(foodCategory);
        return ResponseEntity.status(HttpStatus.OK).body(foodService.create(food));
    }

    @Operation(summary = "takes all foods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "food retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "food not found")
    })
    @GetMapping("/getAll")
    public List<Food> findFood() {
        return foodService.findAll();
    }

    @Operation(summary = "takes a food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "food retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "food not found")
    })
    @GetMapping("/get/{id}")
    public Food findFoodById(@PathVariable Long id) {
        return foodService.findById(id);
    }

    @Operation(summary = "updates food", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "food retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "food not found")
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Food updateFood(@PathVariable Long id,@Valid @RequestBody FoodDto foodDto) {
        foodService.findById(id);
        Food food = FoodMapper.INSTANCE.toFood(foodDto);
        FoodCategory foodCategory = foodCategoryService.findCategoryById(foodDto.getCategoryId());
        food.setCategory(foodCategory);
        food.setId(id);
        return foodService.create(food);
    }

    @Operation(summary = "deletes food", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "food retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "food not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String deleteFoodById(@PathVariable Long id) {
        foodService.findById(id);
        foodService.delete(id);
        return "food was deleted";
    }

}


