package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto.Builder()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setRequest(item.getRequest())
                .build();
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return new Item.Builder()
                .setId(itemDto.getId())
                .setName(itemDto.getName())
                .setDescription(itemDto.getDescription())
                .setAvailable(itemDto.getAvailable())
                .setOwner(user)
                .setRequest(itemDto.getRequest())
                .build();

    }

    public static List<ItemDto> toListItemDto(List<Item> items) {
        return items
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
