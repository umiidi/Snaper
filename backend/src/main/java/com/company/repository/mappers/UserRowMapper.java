package com.company.repository.mappers;

import com.company.models.domain.User;
import com.company.models.enums.Authority;
import com.company.models.enums.Gender;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .authority(Authority.valueOf(rs.getString("authority")))
                .isActivity(rs.getBoolean("is_activity"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .imgUrl(rs.getString("img_url"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .gender(rs.getString("gender") == null ? null : Gender.valueOf(rs.getString("gender")))
                .build();
    }

}
