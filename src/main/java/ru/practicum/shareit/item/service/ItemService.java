package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не владелец вещи");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemResponseDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        BookingResponseDto lastBooking = null;
        BookingResponseDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now())
                    .map(BookingMapper::toResponseDto)
                    .orElse(null);

            nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now())
                    .map(BookingMapper::toResponseDto)
                    .orElse(null);
        }

        List<CommentResponseDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toResponseDto)
                .collect(Collectors.toList());

        return ItemMapper.toItemResponseDto(item, lastBooking, nextBooking, comments);
    }

    public List<ItemDto> getAllItemsByUser(Long userId) {
        List<Item> items = itemRepository.getItemsByOwnerId(userId);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.search(text);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User author = userService.getUserById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        Boolean isBookingComplete = bookingRepository.existsByBookerAndItemAndEndBeforeAndStatus(
                author, item, LocalDateTime.now(), Status.APPROVED);

        if (!isBookingComplete) {
            throw new ValidationException("Оставить отзыв возможно только по истечение срока аренды вещи подтвержденного бронирования");
        }

        Comment comment = new Comment(commentRequestDto.getText(), author, item, LocalDateTime.now());

        comment = commentRepository.save(comment);

        return CommentMapper.toResponseDto(comment);
    }
}
