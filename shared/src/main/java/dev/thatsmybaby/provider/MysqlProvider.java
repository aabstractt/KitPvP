package dev.thatsmybaby.provider;

import lombok.Getter;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MysqlProvider {

    @Getter
    private final static MysqlProvider instance = new MysqlProvider();

    private Map<String, Object> data = new HashMap<>();

    private Connection connection;

    public void init(Map<String, Object> data) throws SQLException {
        if (data.isEmpty()) {
            throw new SQLException("Can't load MySQL");
        }

        this.data = data;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            intentConnect(this.data);

            this.createTables();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1049) {
                createDatabase(this.data);

                this.init(this.data);

                return;
            }

            throw new SQLException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Can't load JDBC Driver", e);
        }
    }

    public void savePlayerStorage(PlayerStorage playerStorage) {
        if (this.connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement;

            if (getPlayerStorage(playerStorage.getName()) == null) {
                preparedStatement = this.connection.prepareStatement("INSERT INTO player_stats(username, kills, betterKillStreak, deaths) VALUES (?, ?, ?, ?)");

                preparedStatement.setString(1, playerStorage.getName());
                preparedStatement.setInt(2, playerStorage.getKills());
                preparedStatement.setInt(3, playerStorage.getBetterKillStreak());
                preparedStatement.setInt(4, playerStorage.getDeaths());
            } else {
                preparedStatement = this.connection.prepareStatement("UPDATE player_stats SET kills = ?, betterKillStreak = ?, deaths = ? WHERE username = ?");

                preparedStatement.setInt(1, playerStorage.getKills());
                preparedStatement.setInt(2, playerStorage.getBetterKillStreak());
                preparedStatement.setInt(3, playerStorage.getDeaths());
                preparedStatement.setString(4, playerStorage.getName());
            }

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                intentConnect(this.data);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public PlayerStorage getPlayerStorage(String name) {
        if (this.connection == null) {
            return null;
        }

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM player_stats WHERE username = ?");

            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();

            PlayerStorage playerStorage = null;

            if (rs.next()) {
                playerStorage = new PlayerStorage(name, rs.getInt("kills"), 0, 0, rs.getInt("betterKillStreak"), rs.getInt("deaths"), null, -1);
            }

            rs.close();
            preparedStatement.close();

            return playerStorage;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                intentConnect(this.data);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private void intentConnect(Map<String, Object> data) throws SQLException {
        if (data.isEmpty()) {
            return;
        }

        Properties properties = new Properties();

        properties.setProperty("user", (String) data.get("user"));
        properties.setProperty("password", (String) data.get("password"));
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("verifyServerCertificate", "false");
        properties.setProperty("useSSL", "false");
        properties.setProperty("requireSSL", "false");

        this.connection = DriverManager.getConnection("jdbc:mysql://" + data.get("host") + ":" + data.get("port") + "/" + data.get("dbname"), properties);
    }

    private void createDatabase(Map<String, Object> data) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + data.get("host") + ":" + data.get("port"),  (String) data.get("user"), (String) data.get("password"));

        Statement statement = this.connection.createStatement();

        statement.executeUpdate("CREATE DATABASE " + data.get("dbname"));

        this.connection.close();

        this.connection = null;
    }

    private void createTables() throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_stats (rowId INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(16), kills INT, betterKillStreak INT, deaths INT)");

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}