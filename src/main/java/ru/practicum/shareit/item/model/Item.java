package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    Long id; // уникальный идентификатор вещи;
    @NotNull
    @NotBlank
    @Size(max = 200)
    String name; // краткое название
    @Size(max = 200)
    String description; // развёрнутое описание
    @NotNull
    Boolean available; // статус о том, доступна или нет вещь для аренды
    Long userId; // id владельца вещи
    Long requestItemId; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос
}
