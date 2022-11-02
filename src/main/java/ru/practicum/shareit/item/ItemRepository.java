package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item);

    Optional<Item> getById(Long itemId);

    List<Item> getAll(Long userId);

    List<Item> getByText(String text);
}
