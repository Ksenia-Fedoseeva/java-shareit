package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotations.ValidationGroup;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;


@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID_HEADER) Long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                              @RequestBody @Valid ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemClient.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                            @PathVariable Long itemId,
                                            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}
