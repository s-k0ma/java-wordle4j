package ru.yandex.practicum.exceptions;

public class WordNoFoundInDictionaryException extends RuntimeException {
    public WordNoFoundInDictionaryException(String message) {
        super(message);
    }
}
