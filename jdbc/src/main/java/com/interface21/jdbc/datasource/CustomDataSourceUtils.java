package com.interface21.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public abstract class CustomDataSourceUtils {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        Connection conn = connectionHolder.get();

        if (conn == null) {
            conn = dataSource.getConnection();
        }

        return conn;
    }

    public static void bindConnection(Connection connection) {
        connectionHolder.set(connection);
    }

    public static void removeConnection() {
        connectionHolder.remove();
    }
}
