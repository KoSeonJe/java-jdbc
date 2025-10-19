package com.techcourse.dao;

import com.interface21.dao.AbstractDao;
import com.interface21.jdbc.core.RowMapper;
import com.techcourse.domain.User;
import com.techcourse.domain.UserHistory;
import com.interface21.jdbc.core.JdbcTemplate;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserHistoryDao extends AbstractDao<UserHistory> {

    private static final Logger log = LoggerFactory.getLogger(UserHistoryDao.class);
    private static final RowMapper<UserHistory> USER_HISTORY_ROW_MAPPER = (rs, rowNum) -> new UserHistory(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("account"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getString("created_by")
    );

    public UserHistoryDao(final DataSource dataSource) {
        super(dataSource);
    }

    public UserHistoryDao(final JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected String getTableName() {
        return "user_history";
    }

    @Override
    protected RowMapper<UserHistory> getRowMapper() {
        return USER_HISTORY_ROW_MAPPER;
    }

    @Override
    protected String[] getAllColumnNames() {
        return new String[]{"id", "user_id", "account", "password", "email", "created_at", "created_by"};
    }

    @Override
    protected String[] getNonIdColumnNames() {
        return Arrays.stream(getAllColumnNames())
                .filter(column -> !column.equals("id"))
                .toArray(String[]::new);
    }

    @Override
    protected Object[] toUpdateAllParams(final UserHistory entity) {
        return new Object[]{entity.getUserId(), entity.getAccount(), entity.getPassword(), entity.getEmail(),
                entity.getCreatedAt(), entity.getCreateBy()};
    }

    public void log(final UserHistory userHistory) {
        insert(userHistory);
    }
}
