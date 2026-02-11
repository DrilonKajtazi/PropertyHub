package com.propertyhub.amenity.repository;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.persistence.sql.SqlRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class AmenityRepository {
    private final JdbcClient jdbcClient;
    private final SqlRegistry sqlRegistry;

    public Amenity save(String name) {
        return jdbcClient.sql(sqlRegistry.get("amenity.save"))
                .param("name", name)
                .query(this::mapRsToAmenity);
    }

    public Amenity findOneById(Long id) {
        return jdbcClient.sql(sqlRegistry.get("amenity.findOneById"))
                .param("id", id)
                .query(this::mapRsToAmenity);
    }

    private Amenity mapRsToAmenity(ResultSet rs) throws SQLException {
        if(!rs.next()) {
            throw new SQLException("ResultSet is empty");
        }
        return new Amenity(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
