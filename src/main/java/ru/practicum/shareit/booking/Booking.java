package ru.practicum.shareit.booking;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Booking {
    Long id; // уникальный идентификатор бронирования
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime start; // дата и время начала бронирования
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime end; // дата и время конца бронирования
    Long itemId; // вещь, которую пользователь бронирует
    Long bookerId; // пользователь, который осуществляет бронирование
    StatusBooking status; // статус бронирования

}
