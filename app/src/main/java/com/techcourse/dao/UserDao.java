package com.techcourse.dao;

import com.interface21.jdbc.core.RowMapper;
import com.techcourse.domain.User;
import com.interface21.jdbc.core.JdbcTemplate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);
    private static final RowMapper<User> USER_ROW_MAPPER =  (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("account"),
            rs.getString("password"),
            rs.getString("email")
    );

    private final JdbcTemplate jdbcTemplate;

    public UserDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final User user) {
        final var sql = "insert into users (account, password, email) values (?, ?, ?)";
        jdbcTemplate.update(sql, user.getAccount(), user.getPassword(), user.getEmail());
    }

    public void update(final User user) {
        final String sql = "update users set password = ?, email = ? where id = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getEmail(), user.getId());
    }

    public List<User> findAll() {
        final String sql = "select id, account, password, email from users";
        return jdbcTemplate.queryForList(sql, USER_ROW_MAPPER);
    }

    public Optional<User> findById(final Long id) {
        final var sql = "select id, account, password, email from users where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, id));
    }

    public Optional<User> findByAccount(final String account) {
        final String sql = "select id, account, password, email from users where account = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, account));
    }
}
