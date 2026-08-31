package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSpecificationDto;
import ru.practicum.shareit.item.model.Item;

@Component
@NoArgsConstructor
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
//        itemDto.setUserId(item.getUserId());
//        itemDto.setRequestItemId(item.getRequestItemId());
        return itemDto;
    }

    public static Item toItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUserId(userId);
        item.setRequestItemId(null);
        return item;
    }

    public static ItemSpecificationDto toItemSpecificationDto(Item item) {
        ItemSpecificationDto itemSpecificationDto = new ItemSpecificationDto();
        itemSpecificationDto.setName(item.getName());
        itemSpecificationDto.setDescription(item.getDescription());
        return itemSpecificationDto;
    }
}
