package ru.practicum.shareit.request.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
public class RequestWithItemDto {
    Long id;
    String description;
    LocalDateTime created;
    List<ItemForRequestDto> items;
}
