package com.company.repository.mappers;

import com.company.models.domain.Post;
import com.company.models.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Post.builder()
                .id(rs.getInt("id"))
                .user(User.builder()
                        .id(rs.getInt("user_id"))
                        .name(rs.getString("user_name"))
                        .surname(rs.getString("surname"))
                        .username(rs.getString("username"))
                        .imgUrl(rs.getString("user_img"))
                        .build()
                )
                .content(rs.getString("content"))
                .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at") == null ? null : rs.getTimestamp("updated_at").toLocalDateTime())
                .views(rs.getInt("views"))
                .build();
    }

}
