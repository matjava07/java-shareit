package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDtoOutput toBookingDto(Booking booking) {
        BookingDtoOutput bookingDtoOutput = new BookingDtoOutput();
        bookingDtoOutput.setId(booking.getId());
        bookingDtoOutput.setStart(booking.getStart());
        bookingDtoOutput.setEnd(booking.getEnd());
        bookingDtoOutput.setStatus(booking.getStatus());
        bookingDtoOutput.setBooker(new BookingDtoOutput.Booker(booking.getBooker().getId()));
        bookingDtoOutput.setItem(new BookingDtoOutput.Item(booking.getItem().getId(), booking.getItem().getName()));
        return bookingDtoOutput;
    }

    public static Booking toBooking(BookingDtoInput bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static List<BookingDtoOutput> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

}
