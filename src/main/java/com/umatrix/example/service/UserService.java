package com.umatrix.example.service;


import com.umatrix.example.configs.security.service.JWTService;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserNotFound;
import com.umatrix.example.models.Order;
import com.umatrix.example.models.Users;
import com.umatrix.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {


    private final OrderService orderService;

    private final UserRepo userRepo;

    private final JWTService jwtService;

    private final AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12) ;

    @Autowired
    public UserService(OrderService orderService, UserRepo userRepo, JWTService jwtService, AuthenticationManager authManager) {
        this.orderService = orderService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(String username, String password) {
        Authentication authenticate
                = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(username);
        }
        return null;
    }

    public boolean checkExistence(String username){
        return userRepo.existsByUsername(username);
    }

    public List<Users> getAllUsers(){
        return userRepo.findAll();
    }

    public Users getUserById(Long id){
        return userRepo.findById(id)
                .orElseThrow(UserNotFound::new);
    }

    public Users updateUser(Users user){
        return userRepo.save(user);
    }

    public void deleteUser(Long id){
        userRepo.deleteById(id);
        for (Order order : orderService.findByUser(id)) {
            try {
                orderService.delete(order.getId());
            } catch (Exception ignore) {
            }
        }
    }

}
