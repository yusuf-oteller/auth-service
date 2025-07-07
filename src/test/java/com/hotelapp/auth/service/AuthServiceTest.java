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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(org.springframework.security.crypto.password.PasswordEncoder.class);
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void register_shouldCreateUserAndReturnToken() {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded_pw");
        when(jwtUtil.generateToken(any(), anyString(), anyString()))
                .thenReturn("mocked-token");

        // Act
        AuthResponseDTO result = authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        assertEquals("mocked-token", result.getToken());
        assertEquals("test@example.com", userCaptor.getValue().getEmail());
        assertEquals("encoded_pw", userCaptor.getValue().getPassword());
        assertEquals(Role.ROLE_CUSTOMER, userCaptor.getValue().getRole());
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        // Arrange
        AuthRequestDTO request = new AuthRequestDTO();
        request.setEmail("existing@example.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // Arrange
        User user = new User(1L, "user@mail.com", "encoded_pw", Role.ROLE_CUSTOMER);
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "encoded_pw")).thenReturn(true);
        when(jwtUtil.generateToken(eq(1L), eq("user@mail.com"), eq("ROLE_CUSTOMER"))).thenReturn("valid-token");

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("user@mail.com");
        login.setPassword("1234");

        // Act
        AuthResponseDTO result = authService.login(login);

        // Assert
        assertEquals("valid-token", result.getToken());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("notfound@example.com");
        login.setPassword("123");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(login));
    }

    @Test
    void login_shouldThrowException_whenPasswordMismatch() {
        // Arrange
        User user = new User(1L, "user@example.com", "encoded_pw", Role.ROLE_CUSTOMER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_pass", "encoded_pw")).thenReturn(false);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("user@example.com");
        login.setPassword("wrong_pass");

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(login));
    }
}
