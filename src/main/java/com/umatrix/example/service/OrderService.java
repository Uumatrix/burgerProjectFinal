package com.umatrix.example.service;

import com.umatrix.example.exceptionHandling.CustomExceptions.OrderNotFound;
import com.umatrix.example.models.Order;
import com.umatrix.example.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderItemService orderItemService;
    private final OrderRepo orderRepo;

    @Autowired
    public OrderService(OrderItemService orderItemService, OrderRepo orderRepo) {
        this.orderItemService = orderItemService;
        this.orderRepo = orderRepo;
    }

    public Order create(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public Order findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(OrderNotFound::new);
    }

    public List<Order> findByUser(Long userId) {
        return orderRepo.findOrdersByUserId(userId);
    }

    public void delete(Long id) {
        orderItemService.deleteAllByOrderId(id);
        orderRepo.deleteById(id);
    }
}


