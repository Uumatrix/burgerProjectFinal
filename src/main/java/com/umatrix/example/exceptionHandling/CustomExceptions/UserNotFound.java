package com.umatrix.example.exceptionHandling.CustomExceptions;


public class UserNotFound extends RuntimeException{
    public UserNotFound() {
        super("User not found");
    }
}
