package ru.practicum.shareit.booking.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.dal.BookingService;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exeption.exeptions.ObjectExcistenceException;
import ru.practicum.shareit.exeption.exeptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public BookingDtoOutput create(BookingDtoInput bookingDto, Long userId) {
        User user = userService.getById(userId);
        Item item = itemService.getByIdForItem(bookingDto.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new ObjectExcistenceException("Пользователь является владельцем");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        if (itemService.getById(booking.getItem().getId(), userId).getAvailable()
                && !booking.getStart().isAfter(booking.getEnd())) {
            return getById(bookingRepository.save(booking).getId(), userId);
        } else {
            throw new ValidationException("Вещь не доступна для аренды");
        }
    }

    @Override
    @Transactional
    public BookingDtoOutput update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = getByIdForBooking(bookingId);
        if (!userId.equals(booking.getBooker().getId())) {
            if (approved && !booking.getStatus().equals(Status.APPROVED)) {
                booking.setStatus(Status.APPROVED);
            } else if (!approved && !booking.getStatus().equals(Status.REJECTED)) {
                booking.setStatus(Status.REJECTED);
            } else {
                throw new ValidationException("Сделка имеет такой статус");
            }
            return getById(bookingId, userId);
        } else {
            throw new ObjectExcistenceException("Попытка изменения статуса сделки не владельцем");
        }
    }

    @Override
    public BookingDtoOutput getById(Long bookingId, Long userId) {
        Booking booking = getByIdForBooking(bookingId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ObjectExcistenceException("Пользователь не относится к сделке");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDtoOutput> getByOwner(Long id, String state) {
        userService.getById(id);
        List<Booking> bookings = List.of();
        switch (State.States.getState(state)) {
            case ALL:
                bookings = bookingRepository.getAllByOwnerAll(id, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllByOwnerCurrent(id, LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingRepository.getAllByOwnerPast(id, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllByOwnerFuture(id, sort);
                break;
            case WAITING:
                bookings = bookingRepository.getAllByOwnerWaiting(id, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllByOwnerRejected(id, sort);
                break;
        }
        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public List<BookingDtoOutput> getByBooker(Long id, String state) {
        userService.getById(id);
        List<Booking> bookings = List.of();
        switch (State.States.getState(state)) {
            case ALL:
                bookings = bookingRepository.getAllByBookerAll(id, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllByBookerCurrent(id, LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingRepository.getAllByBookerPast(id, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllByBookerFuture(id, sort);
                break;
            case WAITING:
                bookings = bookingRepository.getAllByBookerWaiting(id, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllByBookerRejected(id, sort);
                break;
        }
        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public Booking getByIdForBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectExcistenceException("Инструмент не сущестует"));
    }
}
