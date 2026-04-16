package br.com.fiap.foodhub.foodhub.service;

import br.com.fiap.foodhub.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.foodhub.entities.Address;
import br.com.fiap.foodhub.foodhub.entities.User;
import br.com.fiap.foodhub.foodhub.enums.UserType;
import br.com.fiap.foodhub.foodhub.repositories.AddressRepository;
import br.com.fiap.foodhub.foodhub.repositories.UserRepository;
import br.com.fiap.foodhub.foodhub.service.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public void save(UserRequest userRequest) {
        var address = new Address(userRequest.address());
        var savedAddress = addressRepository.save(address);
        var user = new User(userRequest, savedAddress);
        var passwordHash = bCryptPasswordEncoder.encode(userRequest.password());
        user.setPasswordHash(passwordHash);
        var save = userRepository.save(user);
        Assert.state(save == 1, "User not saved");
    }

    public void update(User user) {
        addressRepository.update(user.getAddress());
        var save = userRepository.update(user);
        Assert.state(save == 1, "User not updated");
    }

    public void deleteById(Long id) {
        var save = userRepository.deleteById(id);
        if (save == 0) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public void updatePassword(Long id, String currentPassword, String newPassword) {
        var credentials = userRepository.findCredentialsById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!bCryptPasswordEncoder.matches(currentPassword, credentials.password())) {
            throw new RuntimeException("Current password is incorrect");
        }
        var newPasswordHash = bCryptPasswordEncoder.encode(newPassword);
        var save = userRepository.updatePassword(id, newPasswordHash);
        if (save == 0) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public List<UserResponse> findAll(int page, int size) {
        int offset = (page - 1) * size;
        return userRepository.findAll(offset, size);
    }

    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserResponse> findByFullname(String fullname) {
        if (fullname.isBlank()) {
            throw new RuntimeException("At least one filter must be provided");
        }
        return userRepository.findByFullname(fullname);
    }

    public List<UserResponse> search(UserSearchFilter userSearchFilter) {
        boolean hasFullname = userSearchFilter.fullname() != null && !userSearchFilter.fullname().isBlank();
        boolean hasEmail = userSearchFilter.email() != null && !userSearchFilter.email().isBlank();
        boolean hasUserType = userSearchFilter.userType() != null;

        if (!hasFullname && !hasEmail && !hasUserType) {
            throw new RuntimeException("Search filter is empty");
        }

        return userRepository.findByFullname(userSearchFilter.fullname()).stream().toList();
    }

    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id);
    }
}
