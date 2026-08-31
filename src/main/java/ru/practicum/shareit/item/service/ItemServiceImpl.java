package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSpecificationDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    UserService userService;
    ItemMapper itemMapper;
    Map<Long, Item> items = new HashMap<>();

    public ItemServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                           @Qualifier("itemMapper") ItemMapper itemMapper) {
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    @Override
    public Item createItem(Long userId, ItemDto itemDto) {
        log.info("Получили запрос на добавление новой вещи - " + itemDto + "\n от пользователя c ID - " + userId);
        log.info("Проверяем ID пользоваеля");
        checkUserId(userId);
        normalizeField(itemDto);
        Long itemId = getNextId();
        items.put(itemId, itemMapper.toItem(itemId, userId, itemDto));
        return items.get(itemId);
    }

    @Override
    public Item updateItem(Long userId, Map<String, String> updatesItem, Long itemId) {
        log.info("Получили запрос на редактирование вещи с ID - " + itemId + "\n от пользователя c ID - " + userId);
        log.info("Проверяем ID пользоваеля");
        checkUserId(userId);
        log.info("Проверяем ID вещи и его владельца");
        checkItem(itemId, userId);
        log.info("Проверяем тело запроса");
        checkUpdatesItemMap(updatesItem);
        log.info("Обновление данных");
        return update(updatesItem, itemId);
    }

    @Override
    public Item getItemById(Long itemId) {
        log.info("Получили запрос на получении вещи с Id - " + itemId);
        if (itemId < 0 || !items.containsKey(itemId)) {
            log.warn("Вещи по указанному id не существует - " + itemId);
            throw new ObjectNotFoundException("Вещи по указанному id не существует или некорректный id");
        }
        return items.get(itemId);
    }

    @Override
    public Collection<ItemSpecificationDto> getAllItemsFromUser(Long userId) {
        log.info("Получили запрос на получении всех вещей для пользователя с Id - " + userId);
        checkUserId(userId);
        return items.values().stream()
                .filter((item) -> item.getUserId().equals(userId))
                .map((item) -> itemMapper.toItemSpecificationDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemSpecificationDto> searchItemsForUser(Long userId, String text) {
        log.info("Получили запрос от пользователя с Id - " + userId +
                " на получении всех вещей содержащих строку - " + text);
        checkUserId(userId);
        if (text.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item ->  (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable() == true)
                .map((item) -> itemMapper.toItemSpecificationDto(item))
                .collect(Collectors.toList());
    }

    private void normalizeField(ItemDto itemDto) {
        itemDto.setDescription(itemDto.getDescription().trim());
        itemDto.setName(itemDto.getName().trim());
    }

    private void checkUserId(Long id) {
        if (id < 0 || userService.getUserById(id) == null) {
            log.warn("Пользователя по указаному id не существует - " + id);
            throw new ObjectNotFoundException("Пользователя по указанному id не существует или некорректный id");
        }
    }

    private long getNextId() {
        long counter = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++counter;
    }

    private void checkItem(Long itemId, Long userId) {
        if (!items.containsKey(itemId) || items.get(itemId).getUserId() != userId) {
            log.warn("Не корректные входные данные");
            throw new ObjectNotFoundException("Пользователя по указанному id не существует или некорректный id");
        }
    }

    private void checkUpdatesItemMap(Map<String, String> updatesItem) {
        if (updatesItem.size() > 3) {
            log.warn("Не корректные входные данные");
            throw new ConditionsNotMetException("Не корректное тело запроса");
        }
        for (String s : updatesItem.keySet()) {
            if (!s.equals("name") && !s.equals("description") && !s.equals("available")) {
                log.warn("Не корректные входные данные");
                throw new ConditionsNotMetException("Не корректное тело запроса");
            }
            if (updatesItem.get(s).isBlank()) {
                log.warn("Не корректные входные данные");
                throw new ConditionsNotMetException("Не корректное тело запроса");
            }
        }
    }

    private Item update(Map<String, String> updatesItem, Long itemId) {
        Item item = items.get(itemId);
        for (String s : updatesItem.keySet()) {
            if (s.equals("name")) {
                item.setName(updatesItem.get(s).trim());
            } else if (s.equals("description")) {
                item.setDescription(updatesItem.get(s).trim());
            } else if (s.equals("available")) {
                item.setAvailable(Boolean.parseBoolean(updatesItem.get(s)));
            }
        }
        return items.get(itemId);
    }
}