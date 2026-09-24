package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Map<String, String> updates, Long id);

    Map<String, String> deleteUser(Long id);

    UserDto getUserById(Long id);

    public User getUserEntityById(Long id);

    public User checkObjectOwnerAndGetUserEntityById(Long id);
}