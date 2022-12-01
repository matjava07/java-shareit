package ru.practicum.shareit.booking.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.service.dal.BookingService;
import ru.practicum.shareit.user.valid.Create;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOutput create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @Validated(Create.class) @RequestBody BookingDtoInput bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput update(@RequestParam("approved") Boolean approved,
                                  @PathVariable("bookingId") Long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getById(@PathVariable("bookingId") Long bookingId,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllByOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllByOwner(userId, state);
    }

    @GetMapping()
    public List<BookingDtoOutput> getAllByBooker(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllByBooker(userId, state);
    }

}
