package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public Item create(ItemDto itemDto, Long userId) {
        return itemRepository.create(ItemMapper.toItem(itemDto, userService.getById(userId)));
    }

    public Item update(ItemDto itemDto, Long userId, Long itemId) {
        getItemById(itemId);
        itemDto.setId(itemId);
        return itemRepository.update(ItemMapper.toItem(itemDto, userService.getById(userId)));
    }

    public Item getItemById(Long itemId) {
        return itemRepository.getById(itemId)
                .orElseThrow(() -> new ObjectExcistenceException("Вещи c таким id не существует"));
    }

    public List<Item> getAll(Long userId) {
        return itemRepository.getAll(userId);
    }

    public List<Item> getByText(String text) {
        return itemRepository.getByText(text);
    }
}
