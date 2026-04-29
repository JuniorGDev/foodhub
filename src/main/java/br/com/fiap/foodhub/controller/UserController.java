package br.com.fiap.foodhub.controller;

import br.com.fiap.foodhub.domain.User;
import br.com.fiap.foodhub.dtos.request.UserCredentialsRequest;
import br.com.fiap.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.dtos.request.UserUpdateRequest;
import br.com.fiap.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.enums.UserType;
import br.com.fiap.foodhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Operation(
            summary = "Buscar todos os usuários paginados",
            description = "Retorna uma lista de todos os usuários paginados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários paginados")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public List<UserResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("Accessing findAll method");
        return userService.findAll(page, size);
    }

    @Operation(
            summary = "Buscar o usuário por ID",
            description = "Retorna o usuário com o ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public UserResponse findById(
            @Valid @PathVariable Long id
    ) {
        logger.info("Accessing findById method");
        return userService.findById(id);
    }

    @Operation(
            summary = "Buscar o(s) usuários por Fullname, Email e UserType",
            description = "Retorna o(s) usuário(s) com o(s) filtro(s) fornecido(s)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário(s) encontrado(s)"),
                    @ApiResponse(responseCode = "404", description = "Usuário(s) não encontrado(s)")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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

    @Operation(
            summary = "Trocar senha do usuário",
            description = "Atualiza a senha do usuário informado, validando a senha atual antes da alteração.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/password")
    public void updatePassword(
            @Positive @PathVariable Long id,
            @Valid @RequestBody UserCredentialsRequest userCredentialsRequest
    ) {
        logger.info("Accessing updatePassword method");
        userService.updatePassword(id, userCredentialsRequest.currentPassword(), userCredentialsRequest.newPassword());
    }

    @Operation(
            summary = "Salvar usuário",
            description = "Salvar usuário com os devidos dados preenchidos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public void save(
            @Valid @RequestBody UserRequest userRequest
    ) {
        logger.info("Accessing save method");
        userService.save(userRequest);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualizar dados do usuário por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public void update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        logger.info("Accessing update method");
        userService.update(userUpdateRequest, id);
    }

    @Operation(
            summary = "Deletar usuário",
            description = "Deletar usuário por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Accessing delete method");
        userService.deleteById(id);
    }
}
