package dev.thatsmybaby.provider;

import lombok.Getter;

import java.sql.*;
import java.util.*;

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
                preparedStatement = this.connection.prepareStatement("INSERT INTO player_stats(username, kills, betterKillStreak, deaths, coins, rank_name) VALUES (?, ?, ?, ?, ?, ?)");

                preparedStatement.setString(1, playerStorage.getName());
                preparedStatement.setInt(2, playerStorage.getTotalKills());
                preparedStatement.setInt(3, playerStorage.getBetterKillStreak());
                preparedStatement.setInt(4, playerStorage.getDeaths());
                preparedStatement.setInt(5, playerStorage.getCoins());
                preparedStatement.setString(6, playerStorage.getRankName());
            } else {
                preparedStatement = this.connection.prepareStatement("UPDATE player_stats SET kills = ?, betterKillStreak = ?, deaths = ?, coins = ?, rank_name = ? WHERE username = ?");

                preparedStatement.setInt(1, playerStorage.getTotalKills());
                preparedStatement.setInt(2, playerStorage.getBetterKillStreak());
                preparedStatement.setInt(3, playerStorage.getDeaths());
                preparedStatement.setInt(4, playerStorage.getCoins());
                preparedStatement.setString(5, playerStorage.getRankName());
                preparedStatement.setString(6, playerStorage.getName());
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
                playerStorage = new PlayerStorage(name, rs.getInt("kills"), 0, 0, rs.getInt("betterKillStreak"), rs.getInt("deaths"), rs.getInt("coins"), rs.getString("rank_name"), this.getKitsUnlocked(name), null, -1);
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

    public List<PlayerStorage> getLeaderboard() {
        if (this.connection == null) {
            return new ArrayList<>();
        }

        List<PlayerStorage> list = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT *, @curRank := @curRank + 1 AS `position` FROM `player_stats`, (SELECT @curRank := 0) r ORDER BY `kills` DESC LIMIT 10");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                list.add(new PlayerStorage(rs.getString("username"), rs.getInt("kills"), 0, 0, rs.getInt("betterKillStreak"), rs.getInt("deaths"), rs.getInt("coins"), rs.getString("rank_name"), new ArrayList<>(), null, -1));
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                intentConnect(this.data);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return list;
    }

    public void unlockKit(String name, String kitName) {
        if (this.connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO player_kits(username, kit_name) VALUES (?, ?)");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, kitName);

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

    public List<String> getKitsUnlocked(String name) {
        if (this.connection == null) {
            return new ArrayList<>();
        }

        List<String> kits = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM player_kits WHERE username = ?");

            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                kits.add(rs.getString("kit_name"));
            }

            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                intentConnect(this.data);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return kits;
    }

    public void deleteKills() {
        if (this.connection == null) {
            return;
        }

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE player_stats SET kills = ? WHERE rowId >= 0");

            preparedStatement.setInt(1, 0);

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
        this.connection = DriverManager.getConnection("jdbc:mysql://" + data.get("host") + ":" + data.get("port"), (String) data.get("user"), (String) data.get("password"));

        Statement statement = this.connection.createStatement();

        statement.executeUpdate("CREATE DATABASE " + data.get("dbname"));

        this.connection.close();

        this.connection = null;
    }

    private void createTables() throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_stats (rowId INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(16), kills INT, betterKillStreak INT, deaths INT, coins INT, rank_name TEXT)");

        preparedStatement.executeUpdate();
        preparedStatement.close();

        preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_kits(rowId INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(16), kit_name VARCHAR(16))");

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}