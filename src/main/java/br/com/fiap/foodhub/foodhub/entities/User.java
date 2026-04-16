package br.com.fiap.foodhub.foodhub.entities;

import br.com.fiap.foodhub.foodhub.dtos.request.AddressRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.foodhub.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String fullname;
    private String email;
    private String passwordHash;
    private UserType userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Address address;

    public User (UserRequest userRequest, Address address) {
        this.fullname = userRequest.fullname();
        this.email = userRequest.email();
        this.passwordHash = userRequest.password();
        this.userType = userRequest.userType();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.address = address;
    }
}
