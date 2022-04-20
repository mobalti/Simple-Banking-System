package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    String fileName;
    private static final String url = "jdbc:sqlite:";
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();

    public Database(String fileName) {
        this.fileName = fileName;
        dataSource.setUrl(url + fileName);
    }

    public void createNewDatabase(String fileName) {
        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startQuery(String cardTable) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(cardTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertCard(String cardNumber, String codePIN) {
        String sql = "INSERT INTO card (number, pin) VALUES (?,?);";
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, codePIN);
            preparedStatement.executeUpdate();
            System.out.println("card inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(String cardNumber, String pin) {
        String sql = String.format("SELECT * FROM CARD WHERE number = '%s' AND PIN = '%s';", cardNumber, pin);
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getBalance(String cardNumber, String pin) {
        int balance = -1;
        String sql = String.format("SELECT * FROM card WHERE number =  '%s' AND '%s';", cardNumber, pin);
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                balance = resultSet.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void addIncome(String cardNumber, int income) {
        String updateOrigin = "UPDATE card SET balance = balance + ? WHERE number = ?;";
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(updateOrigin)) {
            preparedStatement.setInt(1, income);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public boolean isCardExist(String cardNumber) {
        String sql = String.format("SELECT * FROM card WHERE number = '%s';", cardNumber);
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void doTransfer(String fromCard, String toCard, int amount) {
        String withdraw = "UPDATE card SET balance = balance - ? WHERE number = ?;";
        String add = "UPDATE card SET balance = balance + ? WHERE number = ?;";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement withdrawPreparedStatement = con.prepareStatement(withdraw);
                 PreparedStatement addPreparedStatement = con.prepareStatement(add)) {

                if (con.getAutoCommit()) {
                    con.setAutoCommit(false);
                }

                withdrawPreparedStatement.setInt(1, amount);
                withdrawPreparedStatement.setString(2, fromCard);
                withdrawPreparedStatement.executeUpdate();


                addPreparedStatement.setInt(1, amount);
                addPreparedStatement.setString(2, toCard);
                addPreparedStatement.executeUpdate();


                con.commit();


            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void closeAccount(String cardNumber) {
        String sql_Delete = "DELETE FROM card WHERE number = ?;";
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql_Delete)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
