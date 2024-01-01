package com.company.repository;

import com.company.models.domain.Post;
import com.company.repository.mappers.PostRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepo {

    private final NamedParameterJdbcTemplate postJdbcTemplate;

    public List<Post> findAll() {
        String query = "SELECT p.*, u.name as user_name, u.surname as surname, u.username AS username, u.img_url AS user_img " +
                "FROM post AS p " +
                "INNER JOIN user AS u ON p.user_id = u.id " +
                "ORDER BY p.created_at DESC";
        return postJdbcTemplate.query(
                query, new PostRowMapper());
    }

    public Optional<Post> find(int id) {
        String query = "SELECT p.*, u.name AS user_name, u.surname as surname, u.username as username, u.img_url AS user_img " +
                "FROM post AS p " +
                "INNER JOIN user AS u ON p.user_id = u.id " +
                "WHERE p.id = :id";
        try {
            return Optional.ofNullable(postJdbcTemplate.queryForObject(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id),
                    new PostRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    public int save(Post p) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO post(user_id, content, created_at, views) " +
                "VALUES (:user_id, :content, :created_at, :views)";
        try {
            postJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("user_id", p.getUser().getId())
                            .addValue("content", p.getContent())
                            .addValue("created_at", p.getCreatedAt())
                            .addValue("views", p.getViews()),
                    keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("post not added!");
        }
    }

    public void update(Post p) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "UPDATE post SET content = :content, updated_at = :updated_at WHERE id = :id";
        try {
            postJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", p.getId())
                            .addValue("content", p.getContent())
                            .addValue("updated_at", p.getUpdatedAt()),
                    keyHolder
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("post not updated!");
        }
    }

    public void views() {
        String query = "UPDATE post SET views = views+1";
        try {
            postJdbcTemplate.update(query, new MapSqlParameterSource());
        } catch (Exception ex) {
            throw new IllegalArgumentException("posts not viewed!");
        }
    }

    public void view(int postId) {
        String query = "UPDATE post SET views = views+1 WHERE id = :post_id";
        try {
            postJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("post_id", postId)
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("post not viewed!");
        }
    }

    public int getUserId(int postId) {
        try {
            String query = "SELECT user_id FROM post WHERE id = :id";
            SqlRowSet rowSet = postJdbcTemplate.queryForRowSet(query,
                    new MapSqlParameterSource()
                            .addValue("id", postId));
            rowSet.next();
            return rowSet.getInt(1);
        } catch (Exception e) {
            return -1;
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM post WHERE id = :id";
        try {
            postJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not deleted!");
        }
    }

}
