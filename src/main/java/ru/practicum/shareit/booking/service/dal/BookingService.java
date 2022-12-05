package ru.practicum.shareit.booking.service.dal;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDtoOutput create(BookingDtoInput bookingDto, Long userId);

    BookingDtoOutput update(Long bookingId, Long userId, Boolean approved);

    BookingDtoOutput getById(Long bookingId, Long userId);

    List<BookingDtoOutput> getByBooker(Long bookerId, String state);

    List<BookingDtoOutput> getByOwner(Long ownerId, String state);

    Booking getByIdForBooking(Long bookingId);
}
