package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("Пользователь создан");
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(userDto)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") @Positive Long userId,
                          @RequestBody @Validated(Update.class) UserDto userDto) {
        if (userId > 0) {
            log.info("Пользователь обновился");
            userDto.setId(userId);
            return UserMapper.toUserDto(userService.update(UserMapper.toUser(userDto)));
        } else {
            throw new ObjectExcistenceException("Пользователь не существует.");
        }
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long userId) {
        log.info("Пользователь с id = {}", userId);
        return UserMapper.toUserDto(userService.getById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Вывод всех созданных пользователей");
        return UserMapper.toListUserDto(userService.getAll());
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long userId) {
        log.info("Удаление пользователя с id = {}", userId);
        userService.deleteById(userId);
    }
}
