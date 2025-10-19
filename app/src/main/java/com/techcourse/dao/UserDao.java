package com.techcourse.dao;

import com.interface21.dao.AbstractDao;
import com.interface21.jdbc.core.RowMapper;
import com.techcourse.domain.User;
import com.interface21.jdbc.core.JdbcTemplate;
import java.util.Arrays;
import java.util.Optional;

import javax.sql.DataSource;

public class UserDao extends AbstractDao<User> {

    private static final RowMapper<User> USER_ROW_MAPPER =  (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("account"),
            rs.getString("password"),
            rs.getString("email")
    );

    public UserDao(final DataSource dataSource) {
        super(dataSource);
    }

    public UserDao(final JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return USER_ROW_MAPPER;
    }

    @Override
    protected String[] getAllColumnNames() {
        return new String[]{"id", "account", "password", "email"};
    }

    @Override
    protected String[] getNonIdColumnNames() {
        return Arrays.stream(getAllColumnNames())
                .filter(column -> !column.equals("id"))
                .toArray(String[]::new);
    }

    @Override
    protected Object[] toUpdateAllParams(final User entity) {
        return new Object[]{entity.getAccount(), entity.getPassword(), entity.getEmail()};
    }

    public Optional<User> findByAccount(final String account) {
        final String sql = "select id, account, password, email from users where account = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, account));
    }
}
