package ru.practicum.shareit.booking.service.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
class BookingServiceImplRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private User booker;
    private User owner;
    private Booking booking;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setName("Чича");
        booker.setEmail("koti@yandex.ru");
        userRepository.save(booker);

        owner = new User();
        owner.setName("Mot");
        owner.setEmail("mot@yandex.ru");
        userRepository.save(owner);

        Item item = new Item();
        item.setOwner(owner);
        item.setName("Мячик");
        item.setAvailable(true);
        item.setDescription("Теннисный мячик");
        itemRepository.save(item);

        booking = new Booking();
        booking.setStart(LocalDateTime.of(2022, 12, 8, 8, 0));
        booking.setEnd(LocalDateTime.of(2022, 12, 9, 8, 0));
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        bookingRepository.save(booking);

        Comment comment = new Comment();
        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.of(2022, 12, 9, 12, 0));
        comment.setText("Превосходный мяч");
        commentRepository.save(comment);
    }

    @Test
    void getAllByOwnerAll() {
        List<Booking> bookings = bookingRepository.getAllByOwnerAll(owner.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void getAllByOwnerPast() {
        List<Booking> bookings = bookingRepository.getAllByOwnerPast(owner.getId(), LocalDateTime.now(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByOwnerFuture() {
        List<Booking> bookings = bookingRepository.getAllByOwnerFuture(owner.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void getAllByOwnerCurrent() {
        List<Booking> bookings = bookingRepository.getAllByOwnerCurrent(owner.getId(), LocalDateTime.now(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByOwnerRejected() {
        List<Booking> bookings = bookingRepository.getAllByOwnerRejected(owner.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByOwnerWaiting() {
        List<Booking> bookings = bookingRepository.getAllByOwnerWaiting(owner.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void getAllByBookerAll() {
        List<Booking> bookings = bookingRepository.getAllByBookerAll(booker.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void getAllByBookerPast() {
        List<Booking> bookings = bookingRepository.getAllByBookerPast(booker.getId(), LocalDateTime.now(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByBookerFuture() {
        List<Booking> bookings = bookingRepository.getAllByBookerFuture(booker.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void getAllByBookerCurrent() {
        List<Booking> bookings = bookingRepository.getAllByBookerCurrent(booker.getId(), LocalDateTime.now(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByBookerRejected() {
        List<Booking> bookings = bookingRepository.getAllByBookerRejected(booker.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertThat(bookings).isEmpty();
    }

    @Test
    void getAllByBookerWaiting() {
        List<Booking> bookings = bookingRepository.getAllByBookerWaiting(booker.getId(),
                PageRequest.of(0, 1, Sort.by(DESC, "start")));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findAll() {
        List<Booking> bookings = bookingRepository.findAll(Sort.by(DESC, "start"));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findApprovedForItems() {
        List<Item> items = itemRepository.findAll();
        booking.setStatus(Status.APPROVED);
        List<Booking> bookings = bookingRepository.findApprovedForItems(items, Sort.by(DESC, "start"));

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }
}