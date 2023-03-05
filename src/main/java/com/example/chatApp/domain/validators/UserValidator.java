package com.example.chatApp.domain.validators;

import com.example.chatApp.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity == null)
            throw new NonExistentEntityException("Id-ul este invalid.");
        if(entity.getFirstName() == "")
            throw new ValidationException("First name can't be empty.");
        if(entity.getLastName() == "")
            throw new ValidationException("Last name can't be empty.");
        if(entity.getEmail() == "")
            throw new ValidationException("Email can't be empty.");
        if(entity.getPassword() == "")
            throw new ValidationException("Password can't be empty.");
    }
}
