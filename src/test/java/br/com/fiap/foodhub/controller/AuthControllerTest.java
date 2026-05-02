package br.com.fiap.foodhub.controller;

import br.com.fiap.foodhub.dtos.request.LoginRequest;
import br.com.fiap.foodhub.dtos.response.LoginResponse;
import br.com.fiap.foodhub.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceStub();
        authController = new AuthController(authService);
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("admin@admin.com", "admin@123");

        LoginResponse response = authController.login(request);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.token());
    }

    @Test
    void shouldThrowExceptionWhenInvalidCredentials() {
        LoginRequest request = new LoginRequest("invalid@email.com", "wrongpassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInvalidPassword() {
        LoginRequest request = new LoginRequest("admin@admin.com", "wrongpassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    static class AuthServiceStub extends AuthService {

        public AuthServiceStub() {
            super(null, null, null);
        }

        @Override
        public LoginResponse login(LoginRequest loginRequest) {
            if ("invalid@email.com".equals(loginRequest.email())) {
                throw new RuntimeException("Invalid credentials");
            }
            if (!"admin@123".equals(loginRequest.password())) {
                throw new RuntimeException("Invalid credentials");
            }
            return new LoginResponse("mock-jwt-token");
        }
    }
}
