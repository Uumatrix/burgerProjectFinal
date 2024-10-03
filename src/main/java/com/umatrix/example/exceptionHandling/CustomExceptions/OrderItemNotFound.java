package com.umatrix.example.exceptionHandling.CustomExceptions;

public class OrderItemNotFound extends RuntimeException {
    public OrderItemNotFound() {super("order item not found");}
}
