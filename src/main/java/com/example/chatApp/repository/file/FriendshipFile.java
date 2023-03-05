package com.example.chatApp.repository.file;


import com.example.chatApp.domain.Friendship;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Long, Friendship>{
    public FriendshipFile(String fileName) {
        super(fileName);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Long userId1 = Long.parseLong(attributes.get(1));
        Long userId2 = Long.parseLong(attributes.get(2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime friendsFrom = LocalDateTime.parse(attributes.get(3), formatter);
        LocalDateTime requestDate = LocalDateTime.parse(attributes.get(5), formatter);
        Friendship friendship = new Friendship(userId1, userId2, attributes.get(4), friendsFrom, requestDate);

        Long friendshipId = Long.parseLong(attributes.get(0));
        friendship.setId(friendshipId);

        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String friendsFrom = entity.getFriendsFrom().format(formatter);
        return entity.getId()+";"+entity.getUserId1()+";"+entity.getUserId2()+";"+friendsFrom;
    }
}
