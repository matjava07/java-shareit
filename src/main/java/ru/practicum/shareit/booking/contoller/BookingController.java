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
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoOutput create(@RequestHeader(USER_ID) Long userId,
                                  @Validated(Create.class) @RequestBody BookingDtoInput bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput update(@RequestParam("approved") Boolean approved,
                                  @PathVariable("bookingId") Long bookingId,
                                  @RequestHeader(USER_ID) Long userId) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getById(@PathVariable("bookingId") Long bookingId,
                                   @RequestHeader(USER_ID) Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getByOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                               @RequestHeader(USER_ID) Long userId) {
        return bookingService.getByOwner(userId, state);
    }

    @GetMapping()
    public List<BookingDtoOutput> getByBooker(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                 @RequestHeader(USER_ID) Long userId) {
        return bookingService.getByBooker(userId, state);
    }

}
