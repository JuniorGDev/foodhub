package br.com.fiap.foodhub.foodhub.controller;

import br.com.fiap.foodhub.foodhub.dtos.request.UserCredentialsRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.foodhub.enums.UserType;
import br.com.fiap.foodhub.foodhub.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public List<UserResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("Accessing findAll method");
        return userService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public Optional<UserResponse> findById(
            @Valid @PathVariable Long id
    ) {
        logger.info("Accessing findById method");
        return userService.findById(id);
    }

    @GetMapping("/search")
    public List<UserResponse> search(
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserType userType
    ) {
        logger.info("Accessing search method");
        var userSearchFilter = new UserSearchFilter(fullname, email, userType);
        return userService.search(userSearchFilter);
    }

    @PatchMapping("/update-password/{id}")
    public void updatePassword(
            @Valid @PathVariable Long id,
            @Valid @RequestBody UserCredentialsRequest userCredentialsRequest
    ) {
        logger.info("Accessing updatePassword method");
        userService.updatePassword(id, userCredentialsRequest.currentPassword(), userCredentialsRequest.newPassword());
    }

    @PostMapping
    public void save(
            @Valid @RequestBody UserRequest userRequest
    ) {
        logger.info("Accessing save method");
        userService.save(userRequest);
    }
}
