package com.umatrix.example.controllers;

import com.umatrix.example.dto.OrderDto;
import com.umatrix.example.exceptionHandling.CustomExceptions.OrderNotFound;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserNotFound;
import com.umatrix.example.exceptionHandling.messageexception.ErrorResponse;
import com.umatrix.example.mapstruct.OrderMapper;
import com.umatrix.example.models.Order;
import com.umatrix.example.models.OrderPayment;
import com.umatrix.example.models.Users;
import com.umatrix.example.service.OrderItemService;
import com.umatrix.example.service.OrderPaymentService;
import com.umatrix.example.service.OrderService;
import com.umatrix.example.service.UserService;
import com.umatrix.example.statics.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/order")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final OrderPaymentService orderPaymentService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, OrderPaymentService orderPaymentService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderPaymentService = orderPaymentService;
    }


    @Operation(summary = "creates an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @PostMapping("/create")
    public Order createOrder(@Valid @RequestBody OrderDto orderDto) {
        Order order = OrderMapper.INSTANCE.toOrder(orderDto);
        order.setOrderDate(LocalDate.now());
        Users user = userService.getUserById(orderDto.getUserId());
        order.setUser(user);
        Order createdOrder = orderService.create(order);
        OrderPayment orderPayment = OrderPayment.builder().payment(false).order(createdOrder).totalPrice(0).build();
        orderPaymentService.create(orderPayment);
        return createdOrder;
    }

    @Operation(summary = "gets all orders", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @Operation(summary = "gets an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @GetMapping("/get/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @Operation(summary = "gets all orders of a user(enter user Id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @GetMapping("/getUserOrders/{id}")
    public List<Order> getUserOrders(@PathVariable Long id) {
        return orderService.findByUser(id);
    }

    @Operation(summary = "updates an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @PutMapping("/update/{id}")
    public Order update(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        Order order = orderService.findById(id);
        order.setAddress(orderDto.getAddress());
        order.setUser(userService.getUserById(orderDto.getUserId()));
        return orderService.create(order);
    }

    @Operation(summary = "deletes an order", description = "also deletes all related food items ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully and order payments"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.findById(id);
        orderPaymentService.delete(orderPaymentService.findByOrderId(id).getId());
        orderService.delete(id);
        return "deleted successfully";
    }
}


