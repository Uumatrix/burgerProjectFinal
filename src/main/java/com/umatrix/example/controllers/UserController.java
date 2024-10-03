package com.umatrix.example.controllers;

import com.umatrix.example.dto.LogInDto;
import com.umatrix.example.dto.UserDto;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserAlreadyExists;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserNotFound;
import com.umatrix.example.exceptionHandling.messageexception.ErrorResponse;
import com.umatrix.example.mapstruct.UserMapper;
import com.umatrix.example.models.Order;
import com.umatrix.example.models.Users;
import com.umatrix.example.service.OrderPaymentService;
import com.umatrix.example.service.OrderService;
import com.umatrix.example.service.UserService;
import com.umatrix.example.statics.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {


    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;

    private final OrderPaymentService orderPaymentService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, OrderService orderService, OrderPaymentService orderPaymentService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
        this.orderPaymentService = orderPaymentService;
    }


    @Operation(summary = "registers a user", description = "user role will be user, and balance will be 100_000")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/registerAsUser")
    public Users registerAsUser(@Valid @RequestBody UserDto userDto) {
        Users user = UserMapper.INSTANCE.toUsers(userDto);
        user.setUserRole(UserRole.ROLE_USER);
        user.setBalance(100_000);
        if (userService.checkExistence(user.getUsername())) {
            throw new UserAlreadyExists();
        }
        return userService.register(user);
    }

    @Operation(summary = "registers a user", description = "user role will be manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/registerAsManager")
    public Users registerAsAdmin(@Valid @RequestBody UserDto userDto) {
        Users user = UserMapper.INSTANCE.toUsers(userDto);
        user.setUserRole(UserRole.ROLE_MANAGER);
        user.setBalance(100_000);
        if (userService.checkExistence(user.getUsername())) {
            throw new UserAlreadyExists();
        }
        return userService.register(user);
    }

    @Operation(summary = "log in", description = "log in is for both users and managers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/login")
    public String logIn(@Valid @RequestBody LogInDto logInDto) {
        return userService.verify(logInDto.getUsername(), logInDto.getPassword());
    }

    @Operation(summary = "gets an user", description = "role does not matter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @GetMapping("/get/{id}")
    public Users findById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "gets all users", description = "only manager can use it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Users> findById() {
        return userService.getAllUsers();
    }

    @Operation(summary = "updates an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PutMapping("/update/{id}")
    public Users update(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        Users users = userService.getUserById(id);
        Users user = UserMapper.INSTANCE.toUsers(userDto);
        user.setBalance(users.getBalance());
        user.setId(users.getId());
        user.setUserRole(users.getUserRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.updateUser(user);
    }

    @Operation(summary = "deletes an user", description = "all related orders will be deleted as well")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.getUserById(id);
        List<Order> orders = orderService.findByUser(id);
        for (Order order : orders) {
            orderService.delete(order.getId());
            orderPaymentService.deleteByOrderId(id);
        }
        userService.deleteUser(id);
        return "user deleted successfully";
    }

    @Operation(summary = "adds balance for an user", description = "enter user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/addBalance/{id}")
    public ResponseEntity<?> addBalance(@PathVariable Long id, double amount) {
        Users users = userService.getUserById(id);
        double balance = userService.getUserById(id).getBalance() + amount;
        users.setBalance(balance);
        userService.updateUser(users);
        return ResponseEntity.status(HttpStatus.OK).body(users.getBalance());
    }

    @Operation(summary = "extracts balance from an user", description = "enter user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/extractBalance/{id}")
    public ResponseEntity<?> deleteBalance(@PathVariable Long id, double amount) {
        Users users = userService.getUserById(id);
        double balance = userService.getUserById(id).getBalance() - amount;
        if (balance < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("balance is not enough"));
        }
        users.setBalance(balance);
        userService.updateUser(users);
        return ResponseEntity.status(HttpStatus.OK).body(users.getBalance());
    }

}
