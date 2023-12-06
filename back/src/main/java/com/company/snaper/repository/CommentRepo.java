package com.company.snaper.repository;

import com.company.snaper.models.domain.Comment;
import com.company.snaper.repository.mappers.CommentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepo {

    private final NamedParameterJdbcTemplate commentJdbcTemplate;

    public Optional<List<Comment>> findAllByPostId(int postId) {
        String query = "SELECT c.*, u.name AS user_name, u.img_url as user_img " +
                "FROM comment AS c " +
                "INNER JOIN user AS u ON c.user_id = u.id " +
                "WHERE post_id = :id AND parent_comment_id IS NULL";
        try {
            return Optional.of(commentJdbcTemplate.query(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", postId),
                    new CommentRowMapper())
            );
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Comment> findAllByParentId(int postId, int parentId) {
        String query = "SELECT c.*, u.name AS user_name, u.img_url AS user_img " +
                "FROM comment AS c " +
                "INNER JOIN user AS u ON c.user_id = u.id " +
                "WHERE post_id = :post_id AND parent_comment_id = :parent_comment_id " +
                "ORDER BY commented_at DESC";
        return commentJdbcTemplate.query(
                query,
                new MapSqlParameterSource()
                        .addValue("post_id", postId)
                        .addValue("parent_comment_id", parentId),
                new CommentRowMapper());
    }

    public int getUserId(int commentId) {
        try {
            String query = "SELECT user_id FROM comment WHERE id = :id";
            SqlRowSet rowSet = commentJdbcTemplate.queryForRowSet(query,
                    new MapSqlParameterSource()
                            .addValue("id", commentId));
            rowSet.next();
            return rowSet.getInt(1);
        } catch (Exception e) {
            return -1;
        }
    }

    public int save(Comment c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO comment(user_id, post_id ,content, parent_comment_id, commented_at) " +
                "VALUES (:user_id, :post_id, :content, :parent_comment_id, :commented_at)";
        try {
            commentJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("user_id", c.getUser().getId())
                            .addValue("post_id", c.getPostId())
                            .addValue("content", c.getContent())
                            .addValue("parent_comment_id", c.getParentCommentId() == 0 ? null : c.getParentCommentId())
                            .addValue("commented_at", c.getCommentedAt()),
                    keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("comment not added!");
        }
    }

    public void update(Comment c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "UPDATE comment SET content = :content, updated_at = :updated_at WHERE id = :id";
        try {
            commentJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", c.getId())
                            .addValue("content", c.getContent())
                            .addValue("updated_at", c.getUpdatedAt()),
                    keyHolder
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("post not updated!");
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM comment WHERE id = :id";
        try {
            commentJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not deleted!");
        }
    }

    public void deleteByParentId(int id) {
        String query = "DELETE FROM comment WHERE parent_comment_id = :id";
        try {
            commentJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not deleted!");
        }
    }

    public void deleteByPostId(int id) {
        String query = "DELETE FROM comment WHERE post_id = :id";
        try {
            commentJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not deleted!");
        }
    }

}
