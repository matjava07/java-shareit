package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final static String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestBody @Validated(Create.class) ItemDto itemDto,
                              @RequestHeader(SHARER_USER_ID) Long userId) {
        return ItemMapper.toItemDto(itemService
                .create(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody @Validated(Update.class) ItemDto itemDto,
                              @RequestHeader(SHARER_USER_ID) Long userId,
                              @PathVariable("itemId") Long itemId) {
        return ItemMapper.toItemDto(itemService
                .update(itemDto, userId, itemId));
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(SHARER_USER_ID) Long userId) {
        return ItemMapper.toListItemDto(itemService.getAll(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestParam("text") String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            return ItemMapper.toListItemDto(itemService.getByText(text.toLowerCase()));
        }
    }
}
