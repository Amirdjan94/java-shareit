package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

@Component
@Data
public class BookingDto {
    Long id; // уникальный идентификатор бронирования
    LocalDateTime start; // дата и время начала бронирования
    LocalDateTime end; // дата и время конца бронирования
    Long itemId; // вещь, которую пользователь бронирует
    Long bookerId; // пользователь, который осуществляет бронирование
    StatusBooking status; // статус бронирования
}
