package br.com.fiap.foodhub.controller;

import br.com.fiap.foodhub.dtos.request.*;
import br.com.fiap.foodhub.dtos.response.AddressResponse;
import br.com.fiap.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.enums.UserType;
import br.com.fiap.foodhub.exceptions.EmailAlreadyExistsException;
import br.com.fiap.foodhub.exceptions.ResourceNotFoundException;
import br.com.fiap.foodhub.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private UserServiceStub userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceStub();
        userController = new UserController(userService);
    }

    @Test
    void shouldReturnUsersWhenFindAll() {
        ResponseEntity<List<UserResponse>> response = userController.findAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnUserWhenFindById() {
        ResponseEntity<UserResponse> response = userController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("John Doe", response.getBody().fullname());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.findById(999L);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void shouldReturnUsersWhenSearchWithFilters() {
        ResponseEntity<List<UserResponse>> response = userController.search("John", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSearchWithEmptyFilters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.search(null, null, null);
        });

        assertEquals("Filtros de busca vazios", exception.getMessage());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        AddressRequest addressRequest = new AddressRequest("Rua Teste", 123, "São Paulo", "01234-567");
        UserRequest request = new UserRequest("New User", "newuser@email.com", "password123", UserType.CLIENT, addressRequest);

        ResponseEntity<Void> response = userController.save(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenCreateUserWithDuplicateEmail() {
        AddressRequest addressRequest = new AddressRequest("Rua Teste", 123, "São Paulo", "01234-567");
        UserRequest request = new UserRequest("New User", "existing@email.com", "password123", UserType.CLIENT, addressRequest);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userController.save(request);
        });

        assertTrue(exception.getMessage().contains("existing@email.com"));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        AddressRequest addressRequest = new AddressRequest("Rua Updated", 456, "Rio de Janeiro", "98765-432");
        UserUpdateRequest request = new UserUpdateRequest("Updated Name", "updated@email.com", addressRequest);

        ResponseEntity<Void> response = userController.update(1L, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistentUser() {
        AddressRequest addressRequest = new AddressRequest("Rua Teste", 123, "São Paulo", "01234-567");
        UserUpdateRequest request = new UserUpdateRequest("Updated Name", "updated@email.com", addressRequest);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.update(999L, request);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserWithDuplicateEmail() {
        AddressRequest addressRequest = new AddressRequest("Rua Teste", 123, "São Paulo", "01234-567");
        UserUpdateRequest request = new UserUpdateRequest("Updated Name", "existing@email.com", addressRequest);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userController.update(2L, request);
        });

        assertTrue(exception.getMessage().contains("existing@email.com"));
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        UserCredentialsRequest request = new UserCredentialsRequest("currentPassword123", "newPassword123");

        ResponseEntity<Void> response = userController.updatePassword(1L, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenUpdatePasswordWithInvalidCurrentPassword() {
        UserCredentialsRequest request = new UserCredentialsRequest("wrongPassword", "newPassword123");

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            userController.updatePassword(1L, request);
        });

        assertEquals("Senha atual incorreta", exception.getMessage());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        ResponseEntity<Void> response = userController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistentUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userController.delete(999L);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    static class UserServiceStub extends UserService {

        private final List<UserResponse> users = new ArrayList<>();

        public UserServiceStub() {
            super(null, null, null);
            AddressResponse address1 = new AddressResponse("Rua Teste", 123, "São Paulo", "01234-567");
            AddressResponse address2 = new AddressResponse("Rua Outra", 456, "Rio de Janeiro", "98765-432");
            users.add(new UserResponse(1L, "John Doe", "john@email.com", UserType.CLIENT, LocalDateTime.now(), LocalDateTime.now(), address1));
            users.add(new UserResponse(2L, "Jane Doe", "jane@email.com", UserType.ADMIN, LocalDateTime.now(), LocalDateTime.now(), address2));
        }

        @Override
        public List<UserResponse> findAll(int page, int size) {
            return users;
        }

        @Override
        public UserResponse findById(Long id) {
            return users.stream()
                    .filter(u -> u.id().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        }

        @Override
        public List<UserResponse> search(UserSearchFilter filter) {
            boolean hasFullname = filter.fullname() != null && !filter.fullname().isBlank();
            boolean hasEmail = filter.email() != null && !filter.email().isBlank();
            boolean hasUserType = filter.userType() != null;

            if (!hasFullname && !hasEmail && !hasUserType) {
                throw new IllegalArgumentException("Filtros de busca vazios");
            }

            return users;
        }

        @Override
        public void save(UserRequest userRequest) {
            if ("existing@email.com".equals(userRequest.email())) {
                throw new EmailAlreadyExistsException(userRequest.email());
            }
        }

        @Override
        public void update(UserUpdateRequest userUpdateRequest, Long id) {
            findById(id);
            if ("existing@email.com".equals(userUpdateRequest.email())) {
                throw new EmailAlreadyExistsException(userUpdateRequest.email());
            }
        }

        @Override
        public void updatePassword(Long id, String currentPassword, String newPassword) {
            findById(id);
            if (!"currentPassword123".equals(currentPassword)) {
                throw new BadCredentialsException("Senha atual incorreta");
            }
        }

        @Override
        public void deleteById(Long id) {
            if (id.equals(999L)) {
                throw new ResourceNotFoundException("Usuário não encontrado");
            }
        }
    }
}
