package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        }
        item.setOwner(userId);
        itemRepository.createItem(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }
        if (!item.getOwner().equals(userId)) {
            throw new AccessDeniedException("Вы не владелец вещи");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        itemRepository.updateItem(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> getAllItemsByUser(Long userId) {
        List<Item> items = itemRepository.getAllItemsByUser(userId);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemRepository.searchItems(text);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
