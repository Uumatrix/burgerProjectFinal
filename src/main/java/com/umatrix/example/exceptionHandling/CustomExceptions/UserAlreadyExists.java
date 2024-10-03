package com.umatrix.example.exceptionHandling.CustomExceptions;

public class UserAlreadyExists extends RuntimeException {
        public UserAlreadyExists() {
            super("this username is already taken");
        }
}
