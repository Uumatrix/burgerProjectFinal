package com.umatrix.example.controllers;


import com.umatrix.example.models.OrderPayment;
import com.umatrix.example.service.OrderPaymentService;
import com.umatrix.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderPayment")
@SecurityRequirement(name = "bearerAuth")
public class OrderPaymentController {


    private final OrderPaymentService orderPaymentService;

    private final OrderService orderService;

    @Autowired
    public OrderPaymentController(OrderPaymentService orderPaymentService, OrderService orderService) {
        this.orderPaymentService = orderPaymentService;
        this.orderService = orderService;
    }

    @Operation(summary = "gets an order payments", description = "order is automatically created when order created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @GetMapping("/getByOrderId/{orderId}")
    public OrderPayment findById(@PathVariable Long orderId) {
        return orderPaymentService.findByOrderId(orderId);
    }

    @Operation(summary = "gets all order payments", description = "order is automatically created when order created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @GetMapping("/getAll")
    public List<OrderPayment> findAll() {
        return orderPaymentService.findAll();
    }


    @Operation(summary = "pays for order payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order not found")
    })
    @PostMapping("/payForOrder/{orderId}")
    public String payForOrder(@PathVariable Long orderId) {
        OrderPayment orderPayment = orderPaymentService.findByOrderId(orderId);
        if (orderPayment.isPayment()) {
            return "this order already is paid";
        }
        String s = orderPaymentService.buyAnOrder(orderId);
        if(s == null) {
            return "order was paid successfully";
        }else if(s.equals("balance")){
            return "balance is not enough to buy";
        }else{
            return "the number of " + s + "is less than required for an order";
        }
    }

}
