package com.umatrix.example.controllers;

import com.umatrix.example.dto.FoodCategoryDto;
import com.umatrix.example.exceptionHandling.CustomExceptions.FoodCategoryNotFound;
import com.umatrix.example.exceptionHandling.messageexception.ErrorResponse;
import com.umatrix.example.mapstruct.FoodCategoryMapper;
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
@RequestMapping("/foodCategory")
@SecurityRequirement(name = "bearerAuth")
public class FoodCategoryController {


    private final FoodCategoryService foodCategoryService;

    private final FoodService foodService;

    @Autowired
    public FoodCategoryController(FoodCategoryService foodCategoryService, FoodService foodService) {
        this.foodCategoryService = foodCategoryService;
        this.foodService = foodService;
    }

    @Operation(summary = "creates a category for food", description = "every food must have a category, and one category might be belonged by many foods, only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/crete")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody FoodCategoryDto foodCategoryDto) {
        FoodCategory foodCategory = FoodCategoryMapper.INSTANCE.toFoodCategory(foodCategoryDto);
        if (foodCategoryService.checkExistence(foodCategoryDto.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Category name already exists"));
        }
        foodCategoryService.create(foodCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodCategory);
    }

    @Operation(summary = "get a category by it is id", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public FoodCategory getById(@PathVariable Long id) {
        return foodCategoryService.findCategoryById(id);
    }

    @Operation(summary = "gets all categories", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<FoodCategory> getAll() {
        return foodCategoryService.findAll();
    }

    @Operation(summary = "update category", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public FoodCategory update(@PathVariable Long id, @Valid @RequestBody FoodCategoryDto foodCategoryDto) {
        FoodCategory category = foodCategoryService.findCategoryById(id);
        category.setName(foodCategoryDto.getName());
        return foodCategoryService.create(category);
    }

    @Operation(summary = "deletes category", description = "all foods with this category will be deleted, only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String delete(@PathVariable Long id) {
        foodCategoryService.findCategoryById(id);
        foodService.deleteAllFoodsByCategory(id);
        foodCategoryService.deleteById(id);
        return "deleted successfully";
    }


}

