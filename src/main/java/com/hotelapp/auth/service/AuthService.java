package com.hotelapp.auth.service;

import com.hotelapp.auth.dto.AuthRequestDTO;
import com.hotelapp.auth.dto.AuthResponseDTO;
import com.hotelapp.auth.dto.LoginRequestDTO;
import com.hotelapp.auth.exception.InvalidCredentialsException;
import com.hotelapp.auth.exception.UserAlreadyExistsException;
import com.hotelapp.auth.model.Role;
import com.hotelapp.auth.model.User;
import com.hotelapp.auth.repository.UserRepository;
import com.hotelapp.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO register(AuthRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(token);
    }
}
