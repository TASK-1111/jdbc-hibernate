package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users(" +
                    "id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, name varchar, lastName varchar, age smallint)");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Таблица уже есть");
        }
    }

    public void dropUsersTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS Users");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Таблицы не существует");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users (name, lastname, age) VALUES (?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User %s %s добавлен в базу\n", name, lastName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id=?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usersList;
    }

    public void cleanUsersTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE Users");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}