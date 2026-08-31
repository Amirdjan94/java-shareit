package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserDto {
    String name; // имя или логин пользователя
    String email; // адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты)
}
