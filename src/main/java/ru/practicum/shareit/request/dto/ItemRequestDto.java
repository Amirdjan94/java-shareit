package ru.practicum.shareit.request.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ItemRequestDto {
    Long id; // уникальный идентификатор запроса
    String description; // текст запроса, содержащий описание требуемой вещи
    Long userRequestorId; // пользователь, создавший запрос
    LocalDateTime created; // дата и время создания запроса
}
