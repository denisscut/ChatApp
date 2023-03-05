package com.example.chatApp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    private Long userId1, userId2;
    private LocalDateTime friendsFrom;
    private String status;
    private LocalDateTime requestDate;

    public Friendship(Long userId1, Long userId2, String status, LocalDateTime friendsFrom, LocalDateTime requestDate) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.friendsFrom = friendsFrom;
        this.status = status;
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
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
