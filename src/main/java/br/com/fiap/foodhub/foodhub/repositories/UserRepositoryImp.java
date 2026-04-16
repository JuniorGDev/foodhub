package br.com.fiap.foodhub.foodhub.repositories;

import br.com.fiap.foodhub.foodhub.dtos.request.UserRequest;
import br.com.fiap.foodhub.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.foodhub.dtos.response.AddressResponse;
import br.com.fiap.foodhub.foodhub.dtos.response.UserCredentials;
import br.com.fiap.foodhub.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.foodhub.entities.User;
import br.com.fiap.foodhub.foodhub.enums.UserType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImp implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<UserResponse> findAll(int offset, int size) {
        return this.jdbcClient
                .sql("SELECT u.id, u.fullname, u.email, u.user_type, u.created_at, u.updated_at, " +
                        "a.street, a.number, a.city, a.zip_code AS zipCode " +
                        "FROM users u " +
                        "JOIN address a ON u.address_id = a.id " +
                        "LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query((rs, rowNum) -> mapUser(rs))
                .list();
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.jdbcClient
                .sql("SELECT u.id, u.fullname, u.email, u.user_type, u.created_at, u.updated_at, " +
                        "a.street, a.number, a.city, a.zip_code AS zipCode " +
                        "FROM users u " +
                        "JOIN address a ON u.address_id = a.id " +
                        "WHERE u.email = :email")
                .param("email", email)
                .query((rs, rowNum) -> mapUser(rs))
                .optional();
    }

    @Override
    public List<UserResponse> findByFullname(String fullname) {
        return this.jdbcClient
                .sql("SELECT u.id, u.fullname, u.email, u.user_type, u.created_at, u.updated_at, " +
                        "a.street, a.number, a.city, a.zip_code AS zipCode " +
                        "FROM users u " +
                        "JOIN address a ON u.address_id = a.id " +
                        "WHERE u.fullname LIKE :fullname")
                .param("fullname", "%" + fullname + "%")
                .query((rs, rowNum) -> mapUser(rs))
                .list();
    }

    @Override
    public List<UserResponse> search(UserSearchFilter filter) {
        StringBuilder sql = new StringBuilder(baseSearchQuery());
        Map<String, Object> params = new HashMap<>();

        appendFullnameFilter(filter, sql, params);
        appendEmailFilter(filter, sql, params);
        appendUserTypeFilter(filter, sql, params);

        var query = jdbcClient.sql(sql.toString());

        for (var entry : params.entrySet()) {
            query = query.param(entry.getKey(), entry.getValue());
        }

        return query.query((rs, rowNum) -> mapUser(rs)).list();
    }

    @Override
    public Optional<UserResponse> findById(Long id) {
        return this.jdbcClient
                .sql("SELECT u.id, u.fullname, u.email, u.user_type, u.created_at, u.updated_at, " +
                        "a.street, a.number, a.city, a.zip_code AS zipCode " +
                        "FROM users u " +
                        "JOIN address a ON u.address_id = a.id " +
                        "WHERE u.id LIKE :id")
                .param("id", id)
                .query((rs, rowNum) -> mapUser(rs))
                .optional();
    }

    @Override
    public Optional<UserCredentials> findCredentialsById(Long id) {
        return this.jdbcClient
                .sql("SELECT id, password_hash FROM users WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> mapUserCredentials(rs))
                .optional();
    }

    @Override
    public Integer save(User user) {
        return this.jdbcClient
                .sql("INSERT INTO users " +
                        "(fullname, email, password_hash, user_type, created_at, updated_at, address_id) " +
                        "VALUES " +
                        "(:fullname, :email, :passwordHash, :userType, :createdAt, :updatedAt, :addressId)")
                .param("fullname", user.getFullname())
                .param("email", user.getEmail())
                .param("passwordHash", user.getPasswordHash())
                .param("userType", user.getUserType())
                .param("createdAt", LocalDateTime.now())
                .param("updated_at", LocalDateTime.now())
                .param("addressId", user.getAddress().getId())
                .update();
    }

    @Override
    public Integer update(User user) {
        return this.jdbcClient
                .sql("UPDATE users SET fullname = :fullname, email = :email, updated_at = :updatedAt WHERE id = :id")
                .param("fullname", user.getFullname())
                .param("email", user.getEmail())
                .param("updated_at", LocalDateTime.now())
                .param("id", user.getId())
                .update();
    }

    @Override
    public Integer deleteById(Long id) {
        return this.jdbcClient
                .sql("DELETE FROM users WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public Integer updatePassword(Long id, String newPassword) {
        return this.jdbcClient
                .sql("UPDATE users SET password_hash = :newPassword, updated_at = :updatedAt WHERE id = :id ")
                .param("id", id)
                .param("newPassword", newPassword)
                .param("updated_at", LocalDateTime.now())
                .update();
    }

    //Mapper manual pois o JdbcClient não suporta o mapeamento de um objeto complexo
    private UserResponse mapUser(ResultSet rs) throws SQLException {

        AddressResponse address = new AddressResponse(
                rs.getString("street"),
                rs.getInt("number"),
                rs.getString("city"),
                rs.getString("zipCode")
        );

        return new UserResponse(
                rs.getLong("id"),
                rs.getString("fullname"),
                rs.getString("email"),
                UserType.valueOf(rs.getString("user_type")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                address
        );
    }

    private UserCredentials mapUserCredentials(ResultSet rs) throws SQLException {
        return new UserCredentials(
                rs.getLong("id"),
                rs.getString("password_hash")
        );
    }

    // Base query for search operations
    private String baseSearchQuery() {
        return """
        SELECT 
            u.id,
            u.fullname,
            u.email,
            u.user_type,
            u.created_at,
            u.updated_at,
            a.street,
            a.number,
            a.city,
            a.zip_code AS zipCode
        FROM users u
        JOIN addresses a ON u.address_id = a.id
        WHERE 1 = 1
        """;
    }

    // Filter methods
    private void appendFullnameFilter(UserSearchFilter filter, StringBuilder sql, Map<String, Object> params) {
        if (filter.fullname() != null && !filter.fullname().isBlank()) {
            sql.append(" AND LOWER(u.fullname) LIKE LOWER(:fullname) ");
            params.put("fullname", "%" + filter.fullname().trim() + "%");
        }
    }

    private void appendEmailFilter(UserSearchFilter filter, StringBuilder sql, Map<String, Object> params) {
        if (filter.email() != null && !filter.email().isBlank()) {
            sql.append(" AND LOWER(u.email) = LOWER(:email) ");
            params.put("email", filter.email().trim());
        }
    }

    private void appendUserTypeFilter(UserSearchFilter filter, StringBuilder sql, Map<String, Object> params) {
        if (filter.userType() != null) {
            sql.append(" AND u.user_type = :userType ");
            params.put("userType", filter.userType().name());
        }
    }
}
