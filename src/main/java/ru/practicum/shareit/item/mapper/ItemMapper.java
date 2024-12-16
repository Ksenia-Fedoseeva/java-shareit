package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item != null) {
            return new ItemDto(
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable());
        }
        return null;
    }
}