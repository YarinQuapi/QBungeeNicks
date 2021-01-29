package xyz.yarinlevi.qbungeenicks.mysql;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.yarinlevi.qbungeenicks.QBungeeNicks;

import java.sql.*;
import java.util.ArrayList;

public class MySQLHandler {
    private final Connection connection;

    public MySQLHandler(QBungeeNicks instance) {
        MySQLConnector connector = new MySQLConnector();
        connection = connector.connect(instance);

        this.createNickListTable();
        this.createPlayerNicksTable();
    }

    public ArrayList<String> getNickList() {
        ArrayList<String> list = new ArrayList<>();

        String sql = "SELECT * FROM `NickList` WHERE `TAKEN`=FALSE";

        try {
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

            if (resultSet.next()) {
                list.add(resultSet.getString("NICK"));

                while (resultSet.next()) {
                    list.add(resultSet.getString("NICK"));
                }
            }

            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public boolean toggleNick(String nick) {
        String sql = "SELECT 1 FROM `NickList` WHERE `NICK`=\"" + nick + "\"";

        try {
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

            if (resultSet.next()) {
                
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    public boolean isInTable(ProxiedPlayer player) {
        String sql = String.format("SELECT 1 FROM `PlayerNicks` WHERE `UUID`=\"%s\"", player.getUniqueId().toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.isBeforeFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void addToTable(ProxiedPlayer player) {
        String sql = String.format("INSERT INTO `PlayerNicks` (`UUID`) VALUES (\"%s\")", player.getUniqueId().toString());

        try {
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void createPlayerNicksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `PlayerNicks` ( `UUID` VARCHAR(36) NOT NULL, `NICK` VARCHAR(16), `HISTORY` TEXT);";

        try {
            Statement statement =  connection.createStatement(); {
                statement.executeUpdate(sql);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNickListTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `NickList` ( `NICK` VARCHAR(16) NOT NULL, `TAKEN` BOOLEAN NOT NULL);";

        try {
            Statement statement =  connection.createStatement(); {
                statement.executeUpdate(sql);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNickToMySQL(ProxiedPlayer player, String nick) {
        String sql = String.format("UPDATE `PlayerNicks` SET `NICK`=\"%s\" WHERE `UUID`=\"%s\";", nick, player.getUniqueId().toString());

        try {
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean removeNick(ProxiedPlayer player) {
        String sql = String.format("UPDATE `PlayerNicks` SET `NICK`=NULL WHERE `UUID`=\"%s\"", player.getUniqueId().toString());

        try {
            connection.prepareStatement(sql).executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean isNicked(ProxiedPlayer player) {
        return !player.getDisplayName().equals(player.getName());
    }

    public String getNick(ProxiedPlayer player) {
        String sql = String.format("SELECT * FROM `PlayerNicks` WHERE `UUID`=\"%s\"", player.getUniqueId().toString());

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getString("NICK") != null ? resultSet.getString("NICK") : player.getName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return player.getName();
    }

    public boolean isNickTaken(String nick) {
        String sql = String.format("SELECT * FROM `PlayerNicks` WHERE `NICK`=\"%s\"", nick);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
