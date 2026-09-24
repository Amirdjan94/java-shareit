package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class BookingDto {
    @NotNull
    private LocalDateTime start; // дата и время начала бронирования
    @NotNull
    private LocalDateTime end; // дата и время конца бронирования
    @NotNull
    private Long itemId; // вещь, которую пользователь бронирует
}