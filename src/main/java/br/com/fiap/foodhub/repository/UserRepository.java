package br.com.fiap.foodhub.repository;

import br.com.fiap.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.dtos.request.UserUpdateRequest;
import br.com.fiap.foodhub.dtos.response.UserCredentials;
import br.com.fiap.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean existsByEmail(String email);
    List<UserResponse> findAll(int offset, int size);
    List<UserResponse> search(UserSearchFilter userSearchFilter);
    Optional<UserResponse> findById(Long id);
    Optional<UserCredentials> findCredentialsById(Long id);
    Optional<UserCredentials> findCredentialsByEmail(String email);
    Optional<Long> findAddressIdByUserId(Long userId);
    Long save(User user);
    Integer update(UserUpdateRequest userUpdateRequest, Long id);
    Integer deleteById(Long id);
    Integer updatePassword(Long id, String newPassword);

}
