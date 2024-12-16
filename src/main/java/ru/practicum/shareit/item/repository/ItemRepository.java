package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(ItemDto itemDto, Long userId);
    Item updateItem(Long itemId, ItemDto itemDto, Long userId);
    Item getItemById(Long itemId);
    List<Item> getAllItemsByUser(Long userId);
    List<Item> searchItems(String text);
}
