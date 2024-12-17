package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
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
