package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    UserService userService;
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    CommentRepository commentRepository;

    public ItemServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                           @Qualifier("itemRepository") ItemRepository itemRepository,
                           @Qualifier("bookingRepository") BookingRepository bookingRepository,
                           @Qualifier("commentRepository") CommentRepository commentRepository
    ) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Получили запрос на добавление новой вещи - " + itemDto + "\n от пользователя c ID - " + userId);
        log.info("Проверяем ID пользоваеля");
        User user = userService.getUserEntityById(userId);
        normalizeField(itemDto);
        Item item = ItemMapper.toItem(user, itemDto);
        Item newItem = itemRepository.save(item);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Map<String, String> updatesItem, Long itemId) {
        log.info("Получили запрос на редактирование вещи с ID - " + itemId + "\n от пользователя c ID - " + userId);
        log.info("Проверяем ID пользоваеля");
        userService.getUserById(userId);
        log.info("Проверяем ID вещи и его владельца");
        checkItem(itemId, userId);
        log.info("Проверяем тело запроса");
        checkUpdatesItemMap(updatesItem);
        log.info("Обновление данных");
        return ItemMapper.toItemDto(update(updatesItem, itemId));
    }

    @Override
    public ItemWithBookingDto getItemById(Long itemId) {
        log.info("Получили запрос на получении вещи с Id - " + itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (itemId < 0 || item.isEmpty()) {
            log.warn("Вещи по указанному id не существует - " + itemId);
            throw new ObjectNotFoundException("Вещи по указанному id не существует или некорректный id - " + itemId);
        }
        List<CommentDto> commentListForItem = getCommetListForItem(itemId)
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment, comment.getUser()))
                .collect(Collectors.toList());
        List<Booking> listBooking = bookingRepository.findBookingByItemAndLocalDateTime(itemId, LocalDateTime.now());
        if (listBooking.size() == 0) {
            return ItemMapper.toItemWithBookingDto(item.get(), null,
                    null, commentListForItem);
        } else if (listBooking.size() == 1) {
            return ItemMapper.toItemWithBookingDto(item.get(), listBooking.get(0).getStart(),
                    null, commentListForItem);
        }
        return ItemMapper.toItemWithBookingDto(item.get(), listBooking.get(0).getStart(),
                listBooking.get(1).getStart(), commentListForItem);
    }

    @Override
    public Item getItemEntityById(Long itemId) {
        log.info("Получили запрос на получении вещи с Id - " + itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (itemId < 0 || item.isEmpty()) {
            log.warn("Вещи по указанному id не существует - " + itemId);
            throw new ObjectNotFoundException("Вещи по указанному id не существует или некорректный id - " + itemId);
        }
        return item.get();
    }

    @Override
    public Collection<ItemListDto> getAllItemsFromUser(Long userId) {
        log.info("Получили запрос на получении всех вещей для пользователя с Id - " + userId);
        userService.getUserById(userId);
        return itemRepository.getListItemForUser(userId)
                .stream()
                .map((item) -> {
                    List<Booking> listBooking = bookingRepository.findBookingByItemAndLocalDateTime(item.getId(), LocalDateTime.now());
                    if (listBooking.size() == 0) {
                        return ItemMapper.toItemListDto(item, null,
                                null);
                    } else if (listBooking.size() == 1) {
                        return ItemMapper.toItemListDto(item, listBooking.get(0).getStart(),
                                listBooking.get(0).getStart());
                    }
                    return ItemMapper.toItemListDto(item, listBooking.get(0).getStart(),
                            listBooking.get(1).getStart());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemSpecificationDto> searchItemsForUser(Long userId, String text) {
        log.info("Получили запрос от пользователя с Id - " + userId +
                " на получении всех вещей содержащих строку - " + text);
        userService.getUserById(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .filter(item -> item.getAvailable() == true &&
                        (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .map((item) -> ItemMapper.toItemSpecificationDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addCommentForItem(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Получили запрос на добавление комментария - " + commentDto);
        User user = userService.getUserEntityById(userId);
        Item item = getItemEntityById(itemId);
        checkBookingItem(user, item);
        Comment comment = CommentMapper.toComment(commentDto, user, item);
        return CommentMapper.toCommentDto(commentRepository.save(comment), user);
    }

    private List<Comment> getCommetListForItem(Long itemId) {
        return commentRepository.getCommetListForItem(itemId);
    }

    private void checkBookingItem(User user, Item item) {
        if (bookingRepository.findBookingByItemAndUser(user.getId(), item.getId(), LocalDateTime.now(),
                StatusBooking.APPROVED).isEmpty()) {
            log.warn("Не корректные входные данные");
            throw new ConditionsNotMetException("Не выполнены условия для комментария");
        }
    }

    private void normalizeField(ItemDto itemDto) {
        itemDto.setDescription(itemDto.getDescription().trim());
        itemDto.setName(itemDto.getName().trim());
    }

    private void checkItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).get();
        if (itemRepository.findById(itemId).isEmpty() || !item.getUser().getId().equals(userId)) {
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
        Item item = itemRepository.findById(itemId).get();
        for (String s : updatesItem.keySet()) {
            if (s.equals("name") && updatesItem.get(s) != null
                    && !updatesItem.get(s).isBlank()) {
                item.setName(updatesItem.get(s).trim());
            } else if (s.equals("description") && updatesItem.get(s) != null
                    && !updatesItem.get(s).isBlank()
                    && updatesItem.get(s).length() <= 200) {
                item.setDescription(updatesItem.get(s).trim());
            } else if (s.equals("available")) {
                item.setAvailable(Boolean.parseBoolean(updatesItem.get(s)));
            } else {
                log.warn("Не корректные входные данные");
                throw new ConditionsNotMetException("Не корректное тело запроса");
            }
        }
        return itemRepository.save(item);
    }
}