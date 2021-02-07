package xyz.yarinlevi.qbungeenicks.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import xyz.yarinlevi.qbungeenicks.QBungeeNicks;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnector {
    @Getter private HikariDataSource dataSource;
    @Getter private String table;
    @Getter private Connection connection;

    public Connection connect(QBungeeNicks instance) {
        String hostName = instance.getPluginConfig().getString("MySql_Hostname");
        table = instance.getPluginConfig().getString("MySql_TableName");
        String database = instance.getPluginConfig().getString("MySql_Database");
        int port = instance.getPluginConfig().getInt("MySql_Port");
        String user = instance.getPluginConfig().getString("MySql_User");
        String pass = instance.getPluginConfig().getString("MySql_Password");
        boolean ssl = instance.getPluginConfig().getBoolean("MySql_UseSSL");

        dataSource = new HikariDataSource();

        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", hostName);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", user);
        dataSource.addDataSourceProperty("password", pass);
        dataSource.addDataSourceProperty("useSSL", ssl);
        dataSource.addDataSourceProperty("autoReconnect", true);
        dataSource.addDataSourceProperty("useUnicode", true);
        dataSource.addDataSourceProperty("characterEncoding", "UTF-8");

        try {
            instance.getLogger().warning("Please await mysql hook...");
            connection = dataSource.getConnection();
            instance.getLogger().info("MySQL Hooked!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
