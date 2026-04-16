package br.com.fiap.foodhub.foodhub.repositories;

import br.com.fiap.foodhub.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.foodhub.dtos.response.UserCredentials;
import br.com.fiap.foodhub.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.foodhub.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<UserResponse> findAll(int offset, int size);
    Optional<UserResponse> findByEmail(String email);
    List<UserResponse> findByFullname(String fullname);
    List<UserResponse> search(UserSearchFilter userSearchFilter);
    Optional<UserResponse> findById(Long id);
    Optional<UserCredentials> findCredentialsById(Long id);
    Integer save(User user);
    Integer update(User user);
    Integer deleteById(Long id);
    Integer updatePassword(Long id, String newPassword);

}
