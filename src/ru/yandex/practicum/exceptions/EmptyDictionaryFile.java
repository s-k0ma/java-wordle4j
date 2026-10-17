package ru.yandex.practicum.exceptions;

public class EmptyDictionaryFile extends RuntimeException {
    public EmptyDictionaryFile(String message) {
        super(message);
    }
}
