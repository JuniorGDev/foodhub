package br.com.fiap.foodhub.controller;

import br.com.fiap.foodhub.dtos.request.LoginRequest;
import br.com.fiap.foodhub.dtos.response.LoginResponse;
import br.com.fiap.foodhub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Operation(
            summary = "Login",
            description = "Faz o login do usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Accessing login method: " + loginRequest.email());
        return authService.login(loginRequest);
    }
}
