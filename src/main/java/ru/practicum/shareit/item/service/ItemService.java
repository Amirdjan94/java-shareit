package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSpecificationDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;

public interface ItemService {

    Item createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, Map<String, String> updatesItem, Long itemId);

    Item getItemById(Long itemId);

    Collection<ItemSpecificationDto> getAllItemsFromUser(Long userId);

    Collection<ItemSpecificationDto> searchItemsForUser(Long userId, String text);
}
