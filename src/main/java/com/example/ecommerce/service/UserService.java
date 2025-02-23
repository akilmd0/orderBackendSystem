package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.dto.UserRequest;
import com.example.ecommerce.dto.UserResponse;
import com.example.ecommerce.repository.UserRepository;

import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setMobNumber(userRequest.getMobNumber());

        System.out.println("the user details are: "+user.getEmail());

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getUserId(), savedUser.getName(), savedUser.getEmail() , savedUser.getMobNumber());
    }
}

