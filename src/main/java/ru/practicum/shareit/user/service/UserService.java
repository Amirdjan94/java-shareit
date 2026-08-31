package ru.practicum.shareit.user.service;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.User;

import java.util.Map;

public interface UserService {
    User createUser(User user);

    User updateUser(Map<String, String> updates, Long id);

    Map<String, String> deleteUser(Long id);

    User getUserById (Long id);
}