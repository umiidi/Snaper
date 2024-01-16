package com.company.repository.mappers;

import com.company.models.domain.Like;
import com.company.models.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Like.builder()
                .id(rs.getInt("id"))
                .user(User.builder()
                        .id(rs.getInt("user_id"))
                        .name(rs.getString("user_name"))
                        .username(rs.getString("username"))
                        .imgUrl(rs.getString("user_img"))
                        .build()
                )
                .postId(rs.getInt("post_id"))
                .build();
    }
}
