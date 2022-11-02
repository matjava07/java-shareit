package ru.practicum.shareit.exeption;

public class DublicateEmailException extends RuntimeException {
    public DublicateEmailException(final String message) {
        super(message);
    }
}
