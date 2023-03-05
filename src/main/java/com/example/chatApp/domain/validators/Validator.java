package com.example.chatApp.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}