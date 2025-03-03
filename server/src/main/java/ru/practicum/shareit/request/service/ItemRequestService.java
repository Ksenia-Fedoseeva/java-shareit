package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemRequestResponseDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userService.getUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        itemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest, Collections.emptyList());
    }

    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));

        List<Item> items = itemRepository.findByRequestId(requestId);

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest, items);
    }

    public List<ItemRequestResponseDto> getUserRequests(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return enrichRequestsWithItems(itemRequests);
    }

    public List<ItemRequestResponseDto> getAllRequests(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);
        return enrichRequestsWithItems(itemRequests);
    }

    private List<ItemRequestResponseDto> enrichRequestsWithItems(List<ItemRequest> itemRequests) {
        Map<Long, List<Item>> itemsByRequest = itemRepository.findByRequestIdIn(
                        itemRequests.stream()
                                .map(ItemRequest::getId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return itemRequests.stream()
                .map(request -> ItemRequestMapper.toItemRequestResponseDto(
                        request, itemsByRequest.getOrDefault(request.getId(), List.of())))
                .collect(Collectors.toList());
    }
}
