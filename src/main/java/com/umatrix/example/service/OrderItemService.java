package com.umatrix.example.service;

import com.umatrix.example.exceptionHandling.CustomExceptions.OrderItemNotFound;
import com.umatrix.example.models.OrderItem;
import com.umatrix.example.repository.OrderItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OrderItemService {


    private final OrderItemRepo orderItemRepo;

    @Autowired
    public OrderItemService(OrderItemRepo orderItemRepo) {
        this.orderItemRepo = orderItemRepo;
    }


    public OrderItem create(OrderItem orderItem) {
        return orderItemRepo.save(orderItem);
    }

    public List<OrderItem> findAll() {
        return orderItemRepo.findAll();
    }

    public OrderItem findById(Long id) {
        return orderItemRepo.findById(id)
                .orElseThrow(OrderItemNotFound::new);
    }

    public void delete(Long id) {
        orderItemRepo.deleteById(id);
    }

    public void deleteAllByOrderId(Long orderId) {
        orderItemRepo.deleteByOrderId(orderId);
    }

    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }
}
