package com.umatrix.example.exceptionHandling.CustomExceptions;

public class FoodCategoryNotFound extends RuntimeException {
    public FoodCategoryNotFound() {super("food category not found");}
}
