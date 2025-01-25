package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotations.ValidationGroup;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody @Valid ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@PathVariable Long itemId,
                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                            @PathVariable Long itemId,
                                            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        return itemService.addComment(userId, itemId, commentRequestDto);
    }
}
