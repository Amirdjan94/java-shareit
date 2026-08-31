package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSpecificationDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    public ItemController(@Qualifier("itemServiceImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody Map<String, String> updatesItem,
                           @PathVariable Long itemId) {
        return itemService.updateItem(userId, updatesItem, itemId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping()
    public Collection<ItemSpecificationDto> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsFromUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemSpecificationDto> searchItemsForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam String text) {
        return itemService.searchItemsForUser(userId, text);
    }
}
