package com.developerdragon.cactuscollectors.sql;

import com.developerdragon.cactuscollectors.CollectorMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQL extends Database {


    public SQL(CollectorMain instance) {
        super(instance);
    }

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            String host = plugin.getConfig().getString("mysql.host");
            String port = plugin.getConfig().getString("mysql.port");
            String database = plugin.getConfig().getString("mysql.database");
            String password = plugin.getConfig().getString("mysql.password");
            String user = plugin.getConfig().getString("mysql.username");
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQL exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQL JBDC library. Google it. Put it in /lib folder.");
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Error:", ex);
        }
        return null;
    }

    public void load() {
        connection = getConnection();
        try {
            Statement s = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS cc_collectors(" +
                    "`id` int(8) NOT NULL AUTO_INCREMENT, PRIMARY KEY(id)," +
                    "`x` int(8), `y` int(8), `z` int(8), `collectedcactus` REAL, `cactusinchunk` int(10), `world` varchar(50) " +
                    ") ENGINE=InnoDB DEFAULT CHARACTER SET utf8;";
            s.executeUpdate(createTable);
            s.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
