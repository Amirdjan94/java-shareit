package ru.practicum.shareit.item.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ItemListDto {
    Long id;
    String name; // краткое название
    String description; // развёрнутое описание
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
}
