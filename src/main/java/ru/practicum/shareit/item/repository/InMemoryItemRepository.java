package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final UserRepository inMemoryUserRepository;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(ItemDto itemDto, Long userId) {
        Item item = new Item();
        item.setId(getNextId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        if (inMemoryUserRepository.getUserById(userId) != null) {
            item.setOwner(userId);
        } else {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }

        if (!item.getOwner().equals(userId)) {
            throw new AccessDeniedException("Вы не владелиц вещи");
        }

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }
        return item;
    }

    @Override
    public List<Item> getAllItemsByUser(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getName() != null && item.getDescription() != null)
                .filter(item -> (item.getName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText)) &&
                        item.getAvailable())
                .collect(Collectors.toList());
    }

    private long getNextId() {
        return items.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}
