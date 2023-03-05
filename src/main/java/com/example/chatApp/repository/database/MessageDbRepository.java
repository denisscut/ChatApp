package com.example.chatApp.repository.database;

import com.example.chatApp.domain.Message;
import com.example.chatApp.repository.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long aLong) {
        Message message = null;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages where \"messageId\" = ?");
            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long messageId = resultSet.getLong("messageId");
                Long user1Id = resultSet.getLong("user1Id");
                Long user2Id = resultSet.getLong("user2Id");
                String messageStr = resultSet.getString("message");
                String dateStr = resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

                message = new Message(user1Id, user2Id, messageStr, date);
                message.setId(messageId);
            }
            return message;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Message save(Message entity) {

        String sql = "insert into messages (user1Id, user2Id, message, date) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUser1Id());
            ps.setLong(2, entity.getUser2Id());
            ps.setString(3, entity.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateStr = entity.getDate().format(formatter);

            ps.setString(4, dateStr);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM messages where \"messageId\" = ?");
            statement.setLong(1, aLong);
            Message message = findOne(aLong);
            statement.executeUpdate();

            return message;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Message update(Message entity) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE messages SET \"user1Id\" = ?, \"user2Id\" = ?, \"message\" = ?, \"date\" = ? WHERE \"messageId\" = ?");
            statement.setLong(1, entity.getUser1Id());
            statement.setLong(2, entity.getUser2Id());
            statement.setString(3, entity.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateStr = entity.getDate().format(formatter);
            statement.setString(4, dateStr);

            statement.executeUpdate();

            return null;
        } catch (SQLException e) {
            return entity;
        }
    }

    @Override
    public Iterable<Message> findAll() {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("messageId");
                Long user1id = resultSet.getLong("user1id");
                Long user2id = resultSet.getLong("user2id");
                String messageStr = resultSet.getString("message");
                String dateStr = resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

                Message message = new Message(user1id, user2id, messageStr, date);
                message.setId(id);
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
