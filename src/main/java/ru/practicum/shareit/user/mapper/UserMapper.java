package ru.practicum.shareit.user.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@NoArgsConstructor
public class UserMapper {
    public static UserDto toItemDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getName());
        return userDto;
    }
}
