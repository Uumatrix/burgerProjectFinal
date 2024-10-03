package com.umatrix.example.service;


import com.umatrix.example.models.*;
import com.umatrix.example.repository.OrderPaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderPaymentService {


    private final OrderPaymentRepo orderPaymentRepo;

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final FoodService foodService;

    private final OrderService orderService;

    @Autowired
    public OrderPaymentService(OrderPaymentRepo orderPaymentRepo, UserService userService, OrderItemService orderItemService, FoodService foodService, OrderService orderService) {
        this.orderPaymentRepo = orderPaymentRepo;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.foodService = foodService;
        this.orderService = orderService;
    }

    public OrderPayment create(OrderPayment orderPayment) {
        return orderPaymentRepo.save(orderPayment);
    }

    public List<OrderPayment> findAll() {
        return orderPaymentRepo.findAll();
    }

    public OrderPayment findByOrderId(Long orderId) {
        orderService.findById(orderId);
        return orderPaymentRepo.findOrderPaymentByOrderId(orderId);
    }

    public void delete(Long id) {
        orderPaymentRepo.deleteById(id);
    }

    public void deleteByOrderId(Long orderId) {
        orderPaymentRepo.deleteOrderPaymentByOrderId(orderId);
    }

    public void refreshTotalPrice(Long orderId) {
        List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);
        double totalPrice = getTotalPrice(orderItems);
        OrderPayment orderPayment = findByOrderId(orderId);
        orderPayment.setTotalPrice(totalPrice);
        orderPaymentRepo.save(orderPayment);
    }

    public String buyAnOrder(Long orderId) {
        OrderPayment orderPayment = orderPaymentRepo.findOrderPaymentByOrderId(orderId);
        Order order = orderService.findById(orderId);
        Users user = order.getUser();
        double balance = user.getBalance();
        List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);
        double totalPrice = getTotalPrice(orderItems);
        double newBalance = extractBalance(totalPrice, balance);
        if (newBalance == -1) {
            return "balance";
        }else{
            if(extractFoodItemFromFood(orderItems) == null){
                user.setBalance(newBalance);
                userService.updateUser(user);
            }else{
                return extractFoodItemFromFood(orderItems);
            }
        }
        orderPayment.setPayment(true);
        orderPayment.setPaymentDate(LocalDate.now());
        orderPayment.setTotalPrice(totalPrice);
        create(orderPayment);
        return null;
    }

    public double getTotalPrice(List<OrderItem> orderItems) {
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public double extractBalance(double totalPrice, double userBalance) {
        if (userBalance < totalPrice) {
            return -1;
        } else {
            return userBalance - totalPrice;
        }
    }

    public String extractFoodItemFromFood(List<OrderItem> orderItems) {
        HashMap<Food, Integer> map = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            Food food = orderItem.getFood();
            Integer foodQuantity = food.getQuantity();
            Integer orderItemQuantity = orderItem.getQuantity();
            if (foodQuantity >= orderItemQuantity) {
                map.put(food, foodQuantity - orderItemQuantity);
            } else {
                return food.getName();
            }
        }
        map.forEach((food, quantity) -> {
           food.setQuantity(quantity);
           foodService.create(food);
        });
        return null;
    }
}
