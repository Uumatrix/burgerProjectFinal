package com.umatrix.example.controllers;

import com.umatrix.example.dto.OrderItemDto;
import com.umatrix.example.mapstruct.OrderItemMapper;
import com.umatrix.example.models.Food;
import com.umatrix.example.models.Order;
import com.umatrix.example.models.OrderItem;
import com.umatrix.example.service.FoodService;
import com.umatrix.example.service.OrderItemService;
import com.umatrix.example.service.OrderPaymentService;
import com.umatrix.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderItem")
@SecurityRequirement(name = "bearerAuth")
public class OrderItemController {

    private final FoodService foodService;

    private final OrderItemService orderItemService;

    private final OrderService orderService;

    private final OrderPaymentService orderPaymentService;

    @Autowired
    public OrderItemController(FoodService foodService, OrderItemService orderItemService, OrderService orderService, OrderPaymentService orderPaymentService) {
        this.foodService = foodService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.orderPaymentService = orderPaymentService;
    }

    @Operation(summary = "creates an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @PostMapping("/create")
    public OrderItem create(@Valid @RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = OrderItemMapper.INSTANCE.toOrderItem(orderItemDto);
        Food food = foodService.findById(orderItemDto.getFoodId());
        double totalPrice = orderItem.getQuantity() * food.getPrice();
        Order order = orderService.findById(orderItemDto.getOrderId());
        orderItem.setTotalPrice(totalPrice);
        orderItem.setFood(food);
        orderItem.setOrder(order);
        OrderItem createdOrderItem = orderItemService.create(orderItem);
        orderPaymentService.refreshTotalPrice(createdOrderItem.getOrder().getId());
        return createdOrderItem;
    }

    @Operation(summary = "gets an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @GetMapping("/get/{id}")
    public OrderItem getOrderItemById(@PathVariable long id) {
        return orderItemService.findById(id);
    }

    @Operation(summary = "gets all order items", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<OrderItem> getAll() {
        return orderItemService.findAll();


    }@Operation(summary = "gets all order items by order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @GetMapping("/getAllByOrderId/{orderId}")
    public List<OrderItem> getAllByOrderId(@PathVariable long orderId) {
        return orderItemService.findAllByOrderId(orderId);
    }



    @Operation(summary = "updates an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @PutMapping("/update/{id}")
    public OrderItem update(@PathVariable Long id, @Valid @RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemService.findById(id);
        Food food = foodService.findById(orderItemDto.getFoodId());
        double totalPrice = orderItemDto.getQuantity() * food.getPrice();
        Order order = orderService.findById(orderItemDto.getOrderId());
        orderItem.setTotalPrice(totalPrice);
        orderItem.setFood(food);
        orderItem.setOrder(order);
        orderItem.setQuantity(orderItemDto.getQuantity());
        OrderItem updatedOrderItem = orderItemService.create(orderItem);
        orderPaymentService.refreshTotalPrice(orderItem.getOrder().getId());
        return updatedOrderItem;
    }

    @Operation(summary = "deletes an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "order item not found")
    })
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        OrderItem orderItem = orderItemService.findById(id);
        orderItemService.delete(id);
        orderPaymentService.refreshTotalPrice(orderItem.getOrder().getId());
        return "deleted successfully";
    }
}
