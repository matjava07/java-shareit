package ru.practicum.shareit.exeption;

public class ObjectExcistenceException extends RuntimeException {
    public ObjectExcistenceException(final String message) {
        super(message);
    }
}
