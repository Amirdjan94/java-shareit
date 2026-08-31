package ru.practicum.shareit.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id; // уникальный идентификатор запроса
    @Size(max = 200)
    String description; // текст запроса, содержащий описание требуемой вещи
    Long userRequestorId; // пользователь, создавший запрос
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime created; // дата и время создания запроса
}

