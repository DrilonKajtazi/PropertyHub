package com.propertyhub.user.repository;

import com.propertyhub.persistence.sql.SqlRegistry;
import com.propertyhub.user.entity.User;
import com.propertyhub.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcClient jdbcClient;
    private final SqlRegistry sqlRegistry;

    public Optional<User> findByUsername(String username) {
        return jdbcClient.sql(sqlRegistry.get("user.findByUsername"))
                .param("username", username)
                .query(this::mapRow)
                .optional();
    }

    public User save(User user) {
        return jdbcClient.sql(sqlRegistry.get("user.save"))
                .params(prepareUserParams(user))
                .query(this::mapRow)
                .single();
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role"))
        );
    }

    private Map<String, Object> prepareUserParams(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("email", user.getEmail());
        params.put("role", user.getRole().name());
        return params;
    }
}
