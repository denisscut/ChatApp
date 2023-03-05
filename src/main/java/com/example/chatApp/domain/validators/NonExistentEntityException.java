package com.example.chatApp.domain.validators;

public class NonExistentEntityException extends RuntimeException{
    public NonExistentEntityException() {
    }

    public NonExistentEntityException(String message) {
        super(message);
    }

    public NonExistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentEntityException(Throwable cause) {
        super(cause);
    }

    public NonExistentEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
