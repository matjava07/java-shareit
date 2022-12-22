package ru.practicum.shareit.booking.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.service.dal.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoOutput create(@RequestHeader(USER_ID) Long userId,
                                   @RequestBody BookingDtoInput bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput update(@RequestParam Boolean approved,
                                   @PathVariable Long bookingId,
                                   @RequestHeader(USER_ID) Long userId) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getById(@PathVariable Long bookingId,
                                    @RequestHeader(USER_ID) Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                @RequestHeader(USER_ID) Long userId,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "20") Integer size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @GetMapping()
    public List<BookingDtoOutput> getAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "20") Integer size) {
        return bookingService.getAllByBooker(userId, state, from, size);
    }

}
