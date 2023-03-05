package com.example.chatApp.repository.database;

import com.example.chatApp.domain.User;
import com.example.chatApp.repository.Repository;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    public UserDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public User findOne(Long aLong) {
        User user = null;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users where \"user_id\" = ?");
            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long idUser = resultSet.getLong("user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                user = new User(firstName, lastName, email, password);
                user.setId(idUser);
            }
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName, email, password);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {

        String sql = "insert into users (firstname, lastname, email, password) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long aLong) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users where \"user_id\" = ?");
            statement.setLong(1, aLong);
            User user = findOne(aLong);
            statement.executeUpdate();

            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User update(User entity) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET \"firstname\" = ?, \"lastname\" = ?, \"email\" = ?, \"password\" = ? WHERE \"user_id\" = ?");
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();

            return null;
        } catch (SQLException e) {
            return entity;
        }
    }
}
