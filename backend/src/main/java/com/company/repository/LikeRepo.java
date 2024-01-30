package com.company.repository;

import com.company.models.domain.Like;
import com.company.repository.mappers.LikeRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepo {

    private final NamedParameterJdbcTemplate likeJdbcTemplate;

    public Optional<List<Like>> getAllLikes(int postId) {
        String query = "SELECT l.*, u.name AS user_name, u.username as username, u.img_url as user_img " +
                "FROM `like` AS l " +
                "INNER JOIN user AS u ON l.user_id = u.id " +
                "WHERE l.post_id = :id";
        try {
            return Optional.of(likeJdbcTemplate.query(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", postId),
                    new LikeRowMapper())
            );
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public int countLikes(int postId) {
        try {
            String query = "SELECT count(*) AS COUNT FROM `like` WHERE post_id = :post_id";
            SqlRowSet rowSet = likeJdbcTemplate.queryForRowSet(query,
                    new MapSqlParameterSource()
                            .addValue("post_id", postId));
            rowSet.next();
            return rowSet.getInt(1);
        } catch (Exception e) {
            return -1;
        }
    }

    public void like(int postId, int userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO `like`(user_id, post_id) " +
                "VALUES (:user_id, :post_id)";
        try {
            likeJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("user_id", userId)
                            .addValue("post_id", postId),
                    keyHolder);
        } catch (Exception ex) {
            throw new IllegalArgumentException("not liked!");
        }
    }

    public void unlike(int postId, int userId) {
        String query = "DELETE FROM `like` WHERE post_id = :post_id AND user_id = :user_id";
        try {
            likeJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("post_id", postId)
                            .addValue("user_id", userId));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not unlike!");
        }
    }

    public void deleteAllLikeByPostId(int postId) {
        String query = "DELETE FROM `like` WHERE post_id = :post_id";
        try {
            likeJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("post_id", postId));
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("not unlike!");
        }
    }

    public boolean isLike(int postId, int userId) {
        String query = "SELECT count(*) AS COUNT FROM `like` WHERE post_id = :post_id AND user_id = :user_id";
        try {
            SqlRowSet rowSet = likeJdbcTemplate.queryForRowSet(query,
                    new MapSqlParameterSource()
                            .addValue("post_id", postId)
                            .addValue("user_id", userId));
            rowSet.next();
            return rowSet.getInt(1) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
