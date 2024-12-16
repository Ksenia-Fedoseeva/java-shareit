package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository inMemoryItemRepository;

    public Item createItem(ItemDto itemDto, Long userId) {
        return inMemoryItemRepository.createItem(itemDto, userId);
    }

    public Item updateItem(Long itemId, ItemDto itemDto, Long userId) {
        return inMemoryItemRepository.updateItem(itemId, itemDto, userId);
    }

    public Item getItemById(Long itemId) {
        return inMemoryItemRepository.getItemById(itemId);
    }

    public List<Item> getAllItemsByUser(Long userId) {
        return inMemoryItemRepository.getAllItemsByUser(userId);
    }

    public List<Item> searchItems(String text) {
        return inMemoryItemRepository.searchItems(text);
    }
}
