package com.unframed.service;

import com.unframed.dto.RegisterRequest;
import com.unframed.dto.UserResponse;
import com.unframed.entity.User;
import com.unframed.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.unframed.exception.EmailAlreadyExistsException;
import com.unframed.dto.LoginRequest;
import com.unframed.exception.InvalidCredentialsException;
import com.unframed.dto.LoginResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import com.unframed.dto.UpdateProfileRequest;
import com.unframed.exception.UserNotFoundException;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setBio(savedUser.getBio());
        response.setProfileImageUrl(savedUser.getProfileImageUrl());

        return response;
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail()
        );

        return new LoginResponse(token);
    }
    public UserResponse getCurrentUser(Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                new UserNotFoundException("User not found")
        );

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setProfileImageUrl(user.getProfileImageUrl());

        return response;
    }
    public UserResponse updateCurrentUser(
            Jwt jwt,
            UpdateProfileRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setProfileImageUrl(request.getProfileImageUrl());

        User updatedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(updatedUser.getId());
        response.setName(updatedUser.getName());
        response.setEmail(updatedUser.getEmail());
        response.setBio(updatedUser.getBio());
        response.setProfileImageUrl(updatedUser.getProfileImageUrl());

        return response;
    }
    public void deleteCurrentUser(Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        userRepository.delete(user);
    }
}