package com.company.repository.mappers;

import com.company.models.domain.Comment;
import com.company.models.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Comment.builder()
                .id(rs.getInt("id"))
                .user(User.builder()
                        .id(rs.getInt("user_id"))
                        .name(rs.getString("user_name"))
                        .username(rs.getString("username"))
                        .imgUrl(rs.getString("user_img"))
                        .build()
                )
                .postId(rs.getInt("post_id"))
                .content(rs.getString("content"))
                .parentCommentId(rs.getInt("parent_comment_id"))
                .commentedAt(rs.getTimestamp("commented_at") == null ? null : rs.getTimestamp("commented_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at") == null ? null : rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

}
