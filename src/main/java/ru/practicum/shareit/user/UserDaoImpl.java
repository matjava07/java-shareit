package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.DublicateEmailException;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User create(User user) {
        if (users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).findFirst().isEmpty()) {
            user.setId(id++);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new DublicateEmailException("Такой email уже занят");
        }
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()).getEmail().equals(user.getEmail())) {
            return doUpdate(user, users.get(user.getId()));
        }
        if (users.values().stream().noneMatch(x -> x.getEmail().equals(user.getEmail()))) {
            return doUpdate(user, users.get(user.getId()));
        }
        throw new DublicateEmailException("Такой email уже занят");
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long userId) {
        users.remove(userId);
    }

    private User doUpdate(User user, User newUser) {
        if (user.getName() != null && !user.getName().isBlank()) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        return newUser;
    }
}