package br.com.fiap.foodhub.service;

import br.com.fiap.foodhub.dtos.request.LoginRequest;
import br.com.fiap.foodhub.dtos.response.LoginResponse;
import br.com.fiap.foodhub.exceptions.ResourceNotFoundException;
import br.com.fiap.foodhub.repository.UserRepository;
import br.com.fiap.foodhub.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordConfig, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordConfig;
        this.jwtService = jwtService;
    }

    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        var credentials = userRepository.findCredentialsByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado"));

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.password(),
                credentials.passwordHash()
        );

        if (!passwordMatches) {
            throw new BadCredentialsException("Senha inválida");
        }

        String token = jwtService.generateToken(credentials);

        return new LoginResponse(token);
    }
}
