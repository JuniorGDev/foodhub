package br.com.fiap.foodhub.service;

import br.com.fiap.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.dtos.request.UserUpdateRequest;
import br.com.fiap.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.domain.Address;
import br.com.fiap.foodhub.domain.User;
import br.com.fiap.foodhub.exceptions.EmailAlreadyExistsException;
import br.com.fiap.foodhub.repository.AddressRepository;
import br.com.fiap.foodhub.repository.UserRepository;
import br.com.fiap.foodhub.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new EmailAlreadyExistsException(userRequest.email());
        }
        var passwordHash = passwordEncoder.encode(userRequest.password());
        var user = new User(userRequest);
        user.setPasswordHash(passwordHash);
        var save = userRepository.save(user);
        var address = new Address(userRequest.address());
        var savedAddress = addressRepository.save(address, save);
        Assert.state(savedAddress == 1, "Usuário não salvo");
    }

    @Transactional
    public void update(UserUpdateRequest userUpdateRequest, Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        addressRepository.update(userUpdateRequest.address(), id);
        var save = userRepository.update(userUpdateRequest, id);
        Assert.state(save == 1, "Usuário não atualizado");
    }

    @Transactional
    public void deleteById(Long id) {
        Long addressId = userRepository.findAddressIdByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        var save = userRepository.deleteById(id);
        if (save == 0) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        addressRepository.deleteById(addressId);
    }

    @Transactional
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        var credentials = userRepository.findCredentialsById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!passwordEncoder.matches(currentPassword, credentials.passwordHash())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        var newPasswordHash = passwordEncoder.encode(newPassword);
        var save = userRepository.updatePassword(id, newPasswordHash);
        if (save == 0) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    public List<UserResponse> findAll(int page, int size) {
        int offset = page > 0 ? (page - 1) * size : 0;
        return userRepository.findAll(offset, size);
    }

    public List<UserResponse> search(UserSearchFilter userSearchFilter) {
        boolean hasFullname = userSearchFilter.fullname() != null && !userSearchFilter.fullname().isBlank();
        boolean hasEmail = userSearchFilter.email() != null && !userSearchFilter.email().isBlank();
        boolean hasUserType = userSearchFilter.userType() != null;

        if (!hasFullname && !hasEmail && !hasUserType) {
            throw new RuntimeException("Filtros de busca vazios");
        }

        return userRepository.search(userSearchFilter);
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
