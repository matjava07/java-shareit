package ru.practicum.shareit.booking.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.user.valid.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) Long userId,
                                         @Validated(Create.class) @RequestBody BookingDtoInput bookingDto) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestParam Boolean approved,
                                         @PathVariable Long bookingId,
                                         @RequestHeader(USER_ID) Long userId) {
        return bookingClient.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable Long bookingId,
                                          @RequestHeader(USER_ID) Long userId) {
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                @RequestHeader(USER_ID) Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "20") Integer size) {
        State.States.getState(state);
        return bookingClient.getAllByOwner(userId, state, from, size);

    }

    @GetMapping()
    public ResponseEntity<Object> getAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(USER_ID) Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "20") Integer size) {

        State.States.getState(state);
        return bookingClient.getAllByBooker(userId, state, from, size);
    }

}
