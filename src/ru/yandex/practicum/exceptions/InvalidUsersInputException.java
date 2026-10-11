package ru.yandex.practicum.exceptions;

public class InvalidUsersInputException extends RuntimeException {
    public InvalidUsersInputException(String message) {
        super(message);
    }
}
