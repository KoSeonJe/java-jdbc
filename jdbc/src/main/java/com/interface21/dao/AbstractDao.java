package com.interface21.dao;

import com.interface21.jdbc.core.JdbcTemplate;
import com.interface21.jdbc.core.RowMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public abstract class AbstractDao<T extends Entity> {

    protected final JdbcTemplate jdbcTemplate;

    protected AbstractDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected abstract String getTableName();
    protected abstract RowMapper<T> getRowMapper();
    protected abstract String[] getAllColumnNames();
    protected abstract String[] getNonIdColumnNames();
    protected abstract Object[] toInsertParams(T entity);
    protected abstract Object[] toUpdateParams(T entity);

    public Optional<T> findById(Long id) {
        String columns = String.join(", ", getAllColumnNames());
        String sql = String.format("SELECT %s FROM %s WHERE id = ?", columns, getTableName());
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getRowMapper(), id));
    }

    public List<T> findAll() {
        String columns = String.join(", ", getAllColumnNames());
        String sql = String.format("SELECT %s FROM %s", columns, getTableName());
        return jdbcTemplate.queryForList(sql, getRowMapper());
    }

    public void insert(T entity) {
        String columns = String.join(", ", getNonIdColumnNames());
        String placeholders = String.join(", ", Collections.nCopies(getNonIdColumnNames().length, "?"));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                getTableName(), columns, placeholders);
        jdbcTemplate.update(sql, toInsertParams(entity));
    }

    public void update(T entity) {
        String[] columns = getNonIdColumnNames();
        String caluse = Arrays.stream(columns)
                .map(col -> col + " = ?")
                .collect(Collectors.joining(", "));
        String sql = String.format("UPDATE %s SET %s WHERE id = ?", getTableName(), caluse);

        Object[] updateParams = toUpdateParams(entity);
        Object[] updateParamsWithId = Arrays.copyOf(updateParams, updateParams.length + 1);
        updateParamsWithId[updateParams.length] = entity.getId();

        jdbcTemplate.update(sql, updateParamsWithId);
    }
}
