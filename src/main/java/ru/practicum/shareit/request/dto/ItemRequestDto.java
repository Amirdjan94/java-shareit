package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Component
@Data
public class ItemRequestDto {
    Long id; // уникальный идентификатор запроса
    String description; // текст запроса, содержащий описание требуемой вещи
    Long userRequestorId; // пользователь, создавший запрос
    LocalDateTime created; // дата и время создания запроса
}
