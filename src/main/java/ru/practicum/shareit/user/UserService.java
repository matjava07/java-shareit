package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.create(user);
    }

    public User update(User user) {
        getById(user.getId());
        return userRepository.update(user);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(Long userId) {
        return userRepository.getById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
