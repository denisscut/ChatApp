package com.example.chatApp.repository.database;

import com.example.chatApp.domain.Friendship;
import com.example.chatApp.repository.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    public FriendshipDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Friendship findOne(Long aLong) {
        Friendship friendship = null;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships where \"friendship_id\" = ?");
            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long idFriendship = resultSet.getLong("friendship_id");
                Long user1id = resultSet.getLong("user1id");
                Long user2id = resultSet.getLong("user2id");
                String status = resultSet.getString("status");
                String friendsFromStr = resultSet.getString("friendsfrom");
                String requestDateStr = resultSet.getString("requestdate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime friendsFrom = LocalDateTime.parse(friendsFromStr, formatter);
                LocalDateTime requestDate = LocalDateTime.parse(requestDateStr, formatter);

                friendship = new Friendship(user1id, user2id, status, friendsFrom, requestDate);
                friendship.setId(idFriendship);
            }
            return friendship;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("friendship_id");
                Long user1id = resultSet.getLong("user1id");
                Long user2id = resultSet.getLong("user2id");
                String status = resultSet.getString("status");
                String friendsFromStr = resultSet.getString("friendsFrom");
                String requestDateStr = resultSet.getString("requestDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime friendsFrom = LocalDateTime.parse(friendsFromStr, formatter);
                LocalDateTime requestDate = LocalDateTime.parse(requestDateStr, formatter);

                Friendship friendship = new Friendship(user1id, user2id, status, friendsFrom, requestDate);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {

        String sql = "insert into friendships (user1id, user2id, status, friendsfrom, requestDate ) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUserId1());
            ps.setLong(2, entity.getUserId2());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String friendsFromStr = entity.getFriendsFrom().format(formatter);
            String requestDateStr = entity.getRequestDate().format(formatter);
            ps.setString(3, entity.getStatus());
            ps.setString(4, friendsFromStr);
            ps.setString(5, requestDateStr);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships where \"friendship_id\" = ?");
            statement.setLong(1, aLong);
            Friendship friendship = findOne(aLong);
            statement.executeUpdate();

            return friendship;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Friendship update(Friendship entity) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE friendships SET \"user1id\" = ?, \"user2id\" = ?, \"friendsfrom\" = ?, \"status\" = ?, \"requestdate\" = ? WHERE \"friendship_id\" = ?");
            statement.setLong(1, entity.getUserId1());
            statement.setLong(2, entity.getUserId2());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String friendsFromStr = entity.getFriendsFrom().format(formatter);
            String requestDateStr = entity.getRequestDate().format(formatter);
            statement.setString(3, friendsFromStr);
            statement.setString(4, entity.getStatus());
            statement.setString(5, requestDateStr);
            statement.setLong(6, entity.getId());
            statement.executeUpdate();

            return null;
        } catch (SQLException e) {
            return entity;
        }
    }
}
