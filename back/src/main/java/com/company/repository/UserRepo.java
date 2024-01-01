package com.company.repository;

import com.company.models.domain.User;
import com.company.repository.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepo {

    private final NamedParameterJdbcTemplate userJdbcTemplate;

    public Optional<User> find(int id) {
        String query = "select * from user where id = :id";
        try {
            return Optional.ofNullable(userJdbcTemplate.queryForObject(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id),
                    new UserRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> find(String username) {
        String query = "select * from user where username = :username";
        try {
            return Optional.ofNullable(userJdbcTemplate.queryForObject(
                    query,
                    new MapSqlParameterSource()
                            .addValue("username", username),
                    new UserRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        String query = "select * from user where username = :username";
        return Optional.ofNullable(userJdbcTemplate.queryForObject(
                query,
                new MapSqlParameterSource()
                        .addValue("username", username),
                new UserRowMapper()));
    }

    public int save(User u) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "insert into user(username, password, authority, is_activity, name, surname, img_url, email, phone, gender) " +
                "values(:username, :password, :authority, :is_activity, :name, :surname, :img_url, :email, :phone, :gender)";
        try {
            userJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("username", u.getUsername())
                            .addValue("password", u.getPassword())
                            .addValue("authority", u.getAuthority().name())
                            .addValue("is_activity", u.getIsActivity())
                            .addValue("name", u.getName())
                            .addValue("surname", u.getSurname())
                            .addValue("img_url", u.getImgUrl())
                            .addValue("email", u.getEmail())
                            .addValue("phone", u.getPhone())
                            .addValue("gender", u.getGender() == null ? null : u.getGender().name()),
                    keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("user not added!");
        }
    }

    public void update(User u) {
        String query = "UPDATE user SET username= :username, password= :password, name= :name, surname= :surname, img_url= :img_url, email= :email, phone= :phone, gender= :gender WHERE id = :id";
        try {
            userJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", u.getId())
                            .addValue("username", u.getUsername())
                            .addValue("password", u.getPassword())
                            .addValue("name", u.getName())
                            .addValue("surname", u.getSurname())
                            .addValue("img_url", u.getImgUrl())
                            .addValue("email", u.getEmail())
                            .addValue("phone", u.getPhone())
                            .addValue("gender", u.getGender() == null ? null : u.getGender().name()));
        } catch (Exception ex) {
            throw new IllegalArgumentException("user not updated!");
        }
    }

    public boolean existsByUsername(String username) {
        String query = "select count(*) as count from user where username = :username ";
        SqlRowSet rowSet = userJdbcTemplate.queryForRowSet(query,
                new MapSqlParameterSource()
                        .addValue("username", username));
        rowSet.next();
        return rowSet.getInt(1) == 1;
    }

    public void deactive(int id) {
        String query = "UPDATE user SET is_activity = false WHERE id = :id";
        try {
            userJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id)
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("user not deactived!");
        }
    }

    public void active(int id) {
        String query = "UPDATE user SET is_activity = true WHERE id = :id";
        try {
            userJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("id", id)
            );
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("user not actived!");
        }
    }


}
