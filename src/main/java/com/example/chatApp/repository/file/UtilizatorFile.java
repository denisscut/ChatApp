package com.example.chatApp.repository.file;

import com.example.chatApp.domain.User;
import java.util.List;

public class UtilizatorFile extends AbstractFileRepository<Long, User> {

    public UtilizatorFile(String fileName) {
        super(fileName);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1),attributes.get(2), attributes.get(3), attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}
