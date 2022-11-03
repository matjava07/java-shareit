package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    private static Long id = 1L;

    @Override
    public Item create(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        final List<Item> userItems = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        userItems.add(item);
        userItemIndex.put(item.getOwner().getId(), userItems);
        return item;
    }

    @Override
    public Item update(Item item) {
        if (items.values().stream().anyMatch(x -> x.getOwner().equals(item.getOwner())
                && x.getId().equals(item.getId()))) {
            return doUpdate(item, items.get(item.getId()));
        } else {
            throw new ObjectExcistenceException("Ошибка в индексах");
        }
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAll(Long userId) {
        return userItemIndex.get(userId);
    }

    @Override
    public List<Item> getByText(String text) {
        return items.values().stream().filter(x -> (x.getDescription().toLowerCase().contains(text)
                        || x.getName().toLowerCase().contains(text)) && x.getAvailable().equals(true))
                .collect(Collectors.toList());

    }

    private Item doUpdate(Item item, Item newItem) {
        if (item.getName() != null && !item.getName().isBlank()) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        if (item.getRequest() != null) {
            newItem.setRequest(item.getRequest());
        }
        return newItem;
    }
}
