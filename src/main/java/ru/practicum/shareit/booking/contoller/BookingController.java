package ru.practicum.shareit.booking.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.service.dal.BookingService;
import ru.practicum.shareit.user.valid.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
    public List<BookingDtoOutput> getAllByOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                @RequestHeader(USER_ID) Long userId,
                                                @PositiveOrZero @RequestParam(value = "from",
                                                        defaultValue = "0") Integer from,
                                                @Positive @RequestParam(value = "size",
                                                        defaultValue = "20") Integer size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @GetMapping()
    public List<BookingDtoOutput> getAllByBooker(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                 @RequestHeader(USER_ID) Long userId,
                                                 @PositiveOrZero @RequestParam(value = "from",
                                                         defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(value = "size",
                                                         defaultValue = "20") Integer size) {
        return bookingService.getAllByBooker(userId, state, from, size);
    }

}
