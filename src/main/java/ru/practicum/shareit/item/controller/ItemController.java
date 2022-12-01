package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.service.dal.CommentService;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoOutput;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.valid.Create;
import ru.practicum.shareit.user.valid.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDtoOutput create(@RequestBody @Validated(Create.class) ItemDtoInput itemDto,
                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService
                .create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOutput update(@RequestBody @Validated(Update.class) ItemDtoInput itemDto,
                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable("itemId") Long itemId) {
        itemDto.setId(itemId);
        return itemService.update(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOutput getById(@PathVariable("itemId") Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDtoOutput> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOutput> getByText(@RequestParam("text") String text) {
        if (text.isBlank()) {
            return List.of();
        } else {
            return itemService.getByText(text.toLowerCase());
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return commentService.create(commentDto, itemId, userId);
    }
}
