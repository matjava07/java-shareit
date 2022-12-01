package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    User update(User user);

    Optional<User> getById(Long userId);

    List<User> getAll();

    void deleteById(Long userId);
}
