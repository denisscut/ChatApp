package com.example.chatApp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FriendshipDTO {
    private Long userId1, userId2;
    private User user1, user2;
    private LocalDateTime friendsFrom;
    private String status;
    private LocalDateTime requestDate;
    private Long id;
    public FriendshipDTO(Long id, Long userId1, Long userId2, String status, LocalDateTime friendsFrom, LocalDateTime requestDate, User user1, User user2) {
        this.id = id;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.friendsFrom = friendsFrom;
        this.status = status;
        this.requestDate = requestDate;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getStatus() {
        return status;
    }
    public String getUser1Name() {
        return this.user1.getFirstName() + " " + this.user1.getLastName();
    }
    public String getUser2Name() {
        return this.user2.getFirstName() + " " + this.user2.getLastName();
    }
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    public String getRequestDateString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return requestDate.format(dateTimeFormatter);
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getId() {
        return id;
    }
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public boolean isUserPartOfTheFriendship(Long userId) {
        return userId1.equals(userId) || userId2.equals(userId);
    }

    public void setUserId1(Long userId1) {
        this.userId1 = userId1;
    }

    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public Long getUserId1() {
        return userId1;
    }

    public Long getUserId2() {
        return userId2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public String getFriendsFromString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return friendsFrom.format(dateTimeFormatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String friendsFrom = this.getFriendsFrom().format(formatter);
        return "Friendship "+this.getId()+" :"+this.getUserId1()+","+this.getUserId2()+","+friendsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        Friendship that = (Friendship) o;
        return getUserId1().equals(that.getUserId1()) &&
                getUserId2().equals(that.getUserId2()) &&
                getFriendsFrom().equals(that.getFriendsFrom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId1(), getUserId2(), getFriendsFrom());
    }
}