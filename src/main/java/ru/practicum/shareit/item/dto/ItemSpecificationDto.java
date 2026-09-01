package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */
@Component
@Data
public class ItemSpecificationDto {
    @NotNull
    @NotBlank
    @Size(max = 200)
    String name; // краткое название
    @Size(max = 200)
    String description; // развёрнутое описание
}