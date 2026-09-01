package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    Long id; // уникальный идентификатор пользователя
    @NotBlank
    String name; // имя или логин пользователя
    @Email
    @NotBlank
    String email; // адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты)
}