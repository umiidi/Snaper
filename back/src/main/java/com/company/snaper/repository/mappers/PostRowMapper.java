package com.company.snaper.repository.mappers;

import com.company.snaper.models.domain.Post;
import com.company.snaper.models.domain.User;
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
                        .username(rs.getString("username"))
                        .imgUrl(rs.getString("user_img"))
                        .build()
                )
                .content(rs.getString("content"))
                .createdAt(rs.getDate("created_at") == null ? null : rs.getDate("created_at").toLocalDate())
                .updatedAt(rs.getDate("updated_at") == null ? null : rs.getDate("updated_at").toLocalDate())
                .views(rs.getInt("views"))
                .build();
    }

}
