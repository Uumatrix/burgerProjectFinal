package com.umatrix.example.exceptionHandling.CustomExceptions;


public class OrderNotFound extends RuntimeException {
    public OrderNotFound() {super("Order not found");}
}
