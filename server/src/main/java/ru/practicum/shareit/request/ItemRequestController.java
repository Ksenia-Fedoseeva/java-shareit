package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestResponseDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getUserItemRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllItemRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequestById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                     @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
