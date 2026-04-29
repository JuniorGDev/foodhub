package br.com.fiap.foodhub.repository;

import br.com.fiap.foodhub.dtos.request.UserSearchFilter;
import br.com.fiap.foodhub.dtos.request.UserUpdateRequest;
import br.com.fiap.foodhub.dtos.response.AddressResponse;
import br.com.fiap.foodhub.dtos.response.UserCredentials;
import br.com.fiap.foodhub.dtos.response.UserResponse;
import br.com.fiap.foodhub.domain.User;
import br.com.fiap.foodhub.enums.UserType;
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
    public boolean existsByEmail(String email) {
        return jdbcClient
                .sql("SELECT COUNT(1) FROM users WHERE LOWER(email) = LOWER(:email)")
                .param("email", email)
                .query(Integer.class)
                .single() > 0;
    }

    @Override
    public List<UserResponse> findAll(int offset, int size) {
        return this.jdbcClient
                .sql("SELECT u.id, u.fullname, u.email, u.user_type, u.created_at, u.updated_at, " +
                        "a.street, a.number, a.city, a.zip_code AS zipCode " +
                        "FROM users u " +
                        "JOIN addresses a ON u.address_id = a.id " +
                        "LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
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
                        "JOIN addresses a ON u.address_id = a.id " +
                        "WHERE u.id = :id")
                .param("id", id)
                .query((rs, rowNum) -> mapUser(rs))
                .optional();
    }

    @Override
    public Optional<UserCredentials> findCredentialsById(Long id) {
        return this.jdbcClient
                .sql("SELECT id, email, user_type, password_hash FROM users WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> mapUserCredentials(rs))
                .optional();
    }

    @Override
    public Optional<UserCredentials> findCredentialsByEmail(String email) {
        return jdbcClient
                .sql("""
                    SELECT id, email, password_hash, user_type
                    FROM users
                    WHERE LOWER(email) = LOWER(:email)
                    """)
                .param("email", email)
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
                .param("userType", user.getUserType().name())
                .param("createdAt", LocalDateTime.now())
                .param("updatedAt", LocalDateTime.now())
                .param("addressId", user.getAddress().getId())
                .update();
    }

    @Override
    public Integer update(UserUpdateRequest userUpdateRequest, Long id) {
        return this.jdbcClient
                .sql("UPDATE users SET fullname = :fullname, email = :email, updated_at = :updatedAt WHERE id = :id")
                .param("fullname", userUpdateRequest.fullname())
                .param("email", userUpdateRequest.email())
                .param("updatedAt", LocalDateTime.now())
                .param("id", id)
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
                .param("updatedAt", LocalDateTime.now())
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
                rs.getString("email"),
                rs.getString("password_hash"),
                UserType.valueOf(rs.getString("user_type"))
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
