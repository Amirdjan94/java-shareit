package ru.practicum.shareit.item.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
public class ItemWithBookingDto {
    Long id;
    String name; // краткое название
    String description; // развёрнутое описание
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    Boolean available;
    List<CommentDto> comments;
}
