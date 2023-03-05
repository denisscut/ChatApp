package com.example.chatApp.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime date;
    private String message;

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Message(Long user1Id, Long user2Id, String message, LocalDateTime date) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.date = date;
        this.message = message;
    }
}
