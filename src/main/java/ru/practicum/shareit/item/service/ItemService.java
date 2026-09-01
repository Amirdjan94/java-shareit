package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSpecificationDto;

import java.util.Collection;
import java.util.Map;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Map<String, String> updatesItem, Long itemId);

    ItemDto getItemById(Long itemId);

    Collection<ItemSpecificationDto> getAllItemsFromUser(Long userId);

    Collection<ItemSpecificationDto> searchItemsForUser(Long userId, String text);
}
