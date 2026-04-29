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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            summary = "Buscar usuários paginados",
            description = "Retorna uma lista paginada de usuários cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuários retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
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
            summary = "Buscar usuário por ID",
            description = "Retorna os dados do usuário correspondente ao ID informado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autenticado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
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
            summary = "Pesquisar usuários",
            description = "Pesquisa usuários por nome completo, e-mail e/ou tipo de usuário.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuários encontrados",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Filtros inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuários não encontrados", content = @Content)
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
            summary = "Alterar senha do usuário",
            description = "Atualiza a senha do usuário validando a senha atual.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Senha alterada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autenticado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
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
            summary = "Cadastrar usuário",
            description = "Realiza o cadastro de um novo usuário no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autenticado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "E-mail já cadastrado",
                            content = @Content
                    )
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
            description = "Atualiza os dados cadastrais do usuário informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
                    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content)
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
            description = "Remove o usuário informado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Accessing delete method");
        userService.deleteById(id);
    }
}
