package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    Map<Long, User> users = new HashMap<>();

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Получили запрос на добавление пользователя");
        User user = UserMapper.toUser(userDto);
        checkEmailNewUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("В список добавили пользователя - " + user.toString());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Map<String, String> updates, Long id) {
        log.info("Получили запрос на обновение данных пользоватля");
        checkUpdates(updates);
        checkUserId(id);
        User user = users.get(id);
        updateFields(user, updates);
        return UserMapper.toUserDto(user);
    }

    @Override
    public Map<String, String> deleteUser(Long id) {
        log.info("Получили запрос на удаление пользоватля с ID-" + id);
        checkUserId(id);
        users.remove(id);
        log.info("Удалили пользователя с ID-" + id);
        return Map.of("status", "success",
                "operation", "Delete user");
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Получили запрос на передачу пользоватля с ID-" + id);
        checkUserId(id);
        log.info("Передали пользователя по ID-" + id);
        return UserMapper.toUserDto(users.get(id));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkEmailNewUser(User user) {
        for (User value : users.values()) {
            if (user.getEmail().equals(value.getEmail())) {
                log.warn("Имейл уже используется");
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
    }

    private void checkEmailCurrentUser(User user, String email) {
        for (User value : users.values()) {
            if (!user.getId().equals(value.getId()) && value.getEmail().equals(email)) {
                log.warn("Имейл уже используется");
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
    }

    private void checkUserId(Long id) {
        if (id == null || id < 0 || !users.keySet().contains(id)) {
            log.warn("Пользователя по указаному id не существует - " + id);
            throw new ObjectNotFoundException("Пользователя по указаному id не существует - " + id);
        }
    }

    private void checkUpdates(Map<String, String> updates) {
        if (updates.isEmpty() || updates.size() > 2) {
            throw new ConditionsNotMetException("Не корректные входные данные");
        }
        for (String s : updates.keySet()) {
            if (!s.equals("name") && !s.equals("email")) {
                log.warn("Не корректные входные данные - !s.equals(\"name\") = " + !s.equals("name") + "; \n " +
                        "!s.equals(\"email\") = " + !s.equals("email"));
                throw new ConditionsNotMetException("Не корректные входные данные");
            }
        }
    }

    private void updateFields(User user, Map<String, String> updates) {
        for (String s : updates.keySet()) {
            if (s.equals("name") && updates.get(s) != null
                    && !updates.get(s).isBlank()) {
                user.setName(updates.get(s));
            } else if (s.equals("email") && updates.get(s) != null
                    && !updates.get(s).isBlank()
                    && updates.get(s).contains("@")) {
                checkEmailCurrentUser(user, updates.get(s));
                user.setEmail(updates.get(s));
            } else {
                log.warn("Не корректные входные данные");
                throw new ConditionsNotMetException("Не корректное тело запроса");
            }
        }
    }
}
