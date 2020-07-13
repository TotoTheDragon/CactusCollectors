package com.developerdragon.cactuscollectors.database;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.objects.CactusCollector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SQLHandler implements IDataHandler {

    private String table = "cc_collectors";

    Connection connection;

    private static CollectorMain plugin = CollectorMain.getInstance();

    public SQLHandler(){
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

    public void initialize() {
        connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet rs = ps.executeQuery();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stopConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean addCollector(CactusCollector collector) {
        if (collector == null) return false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table + "(x,y,z,collectedcactus,cactusinchunk,world) VALUES(?,?,?,?,?,?);");
            preparedStatement.setInt(1, collector.getHashLocation().getX());
            preparedStatement.setInt(2, collector.getHashLocation().getY());
            preparedStatement.setInt(3, collector.getHashLocation().getZ());
            preparedStatement.setDouble(4, collector.getCactusStored());
            preparedStatement.setInt(5, collector.getCactusInChunk());
            preparedStatement.setString(6, collector.getHashLocation().getWorldName());
            preparedStatement.execute();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean removeCollector(CactusCollector collector) {
        if (collector == null) return false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table + " WHERE x=? AND y=? AND z=? AND world=?;");
            preparedStatement.setInt(1, collector.getHashLocation().getX());
            preparedStatement.setInt(2, collector.getHashLocation().getY());
            preparedStatement.setInt(3, collector.getHashLocation().getZ());
            preparedStatement.setString(4, collector.getHashLocation().getWorldName());
            preparedStatement.execute();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean updateCollector(CactusCollector collector) {
        if (collector == null) return false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + table + " WHERE x=? AND y=? AND z=? AND world=? SET collectedcactus=? AND cactusinchunk=?;");
            preparedStatement.setInt(1, collector.getHashLocation().getX());
            preparedStatement.setInt(2, collector.getHashLocation().getY());
            preparedStatement.setInt(3, collector.getHashLocation().getZ());
            preparedStatement.setString(4, collector.getHashLocation().getWorldName());
            preparedStatement.setDouble(5, collector.getCactusStored());
            preparedStatement.setInt(6, collector.getCactusInChunk());
            preparedStatement.execute();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean bulkUpdate(List<CactusCollector> collectors) {
        collectors = collectors.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (collectors.isEmpty()) return false;

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + table + " SET collectedcactus=?,cactusinchunk=? WHERE x=? AND y=? AND z=? AND world=? ;");
            for (CactusCollector collector : collectors) {
                preparedStatement.setDouble(1, collector.getCactusStored());
                preparedStatement.setInt(2, collector.getCactusInChunk());
                preparedStatement.setInt(3, collector.getHashLocation().getX());
                preparedStatement.setInt(4, collector.getHashLocation().getY());
                preparedStatement.setInt(5, collector.getHashLocation().getZ());
                preparedStatement.setString(6, collector.getHashLocation().getWorldName());
                preparedStatement.addBatch();
            }
            int[] output = preparedStatement.executeBatch();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<CactusCollector> getCollectors() {
        List<CactusCollector> output = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                output.add(new CactusCollector(resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"), resultSet.getString("world"), resultSet.getDouble("collectedcactus"), resultSet.getInt("cactusinchunk")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return output;
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

}
