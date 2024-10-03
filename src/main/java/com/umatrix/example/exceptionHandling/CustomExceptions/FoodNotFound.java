package com.umatrix.example.exceptionHandling.CustomExceptions;

public class FoodNotFound extends RuntimeException{
    public FoodNotFound() {super("Food Not Found");}
}
