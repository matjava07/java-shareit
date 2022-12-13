package ru.practicum.shareit.booking.service.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.dal.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoOutput;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTestIntegration {

    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private User ownerNew;
    private User bookerNew;
    private ItemDtoInput itemDtoInput;
    private BookingDtoInput bookingDtoInput;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setName("Mot");
        owner.setEmail("mot@yandex.ru");
        ownerNew = userService.create(owner);

        User booker = new User();
        booker.setName("Чича");
        booker.setEmail("koti@yandex.ru");
        bookerNew = userService.create(booker);

        itemDtoInput = new ItemDtoInput();
        itemDtoInput.setName("Мячик");
        itemDtoInput.setDescription("Теннисный мячик");
        itemDtoInput.setAvailable(true);
        ItemDtoOutput itemDtoOutput = itemService.create(itemDtoInput, ownerNew.getId());

        bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(itemDtoOutput.getId());
        bookingDtoInput.setStart(LocalDateTime.of(2022, 12, 8, 8, 0));
        bookingDtoInput.setEnd(LocalDateTime.of(2022, 12, 10, 8, 0));

    }

    @Test
    void createTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDtoOutput.getId())
                .getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertEquals(bookingDtoInput.getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoInput.getStart(), booking.getStart());
        assertEquals(bookingDtoInput.getEnd(), booking.getEnd());
    }

    @Test
    void updateTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());

        bookingService.update(bookingDtoOutput.getId(), ownerNew.getId(), false);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDtoOutput.getId())
                .getSingleResult();

        assertEquals(bookingDtoOutput.getId(), booking.getId());
        assertEquals(bookingDtoInput.getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoInput.getStart(), booking.getStart());
        assertEquals(bookingDtoInput.getEnd(), booking.getEnd());
        assertEquals(Status.REJECTED, booking.getStatus());
    }

    @Test
    void getByIdTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());
        BookingDtoOutput bookingDtoOutput1 = bookingService.getById(bookingDtoOutput.getId(), ownerNew.getId());

        assertEquals(bookingDtoOutput.getId(), bookingDtoOutput1.getId());
        assertEquals(bookingDtoOutput.getItem().getId(), bookingDtoOutput1.getItem().getId());
        assertEquals(bookingDtoOutput.getItem().getName(), bookingDtoOutput1.getItem().getName());
        assertEquals(bookingDtoOutput.getStart(), bookingDtoOutput1.getStart());
        assertEquals(bookingDtoOutput.getEnd(), bookingDtoOutput1.getEnd());
        assertEquals(bookingDtoOutput.getBooker().getId(), bookingDtoOutput1.getBooker().getId());
    }

    @Test
    void getAllByOwnerTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());
        List<BookingDtoOutput> bookingDtoOutputList = bookingService
                .getAllByOwner(ownerNew.getId(), "ALL", 0, 1);

        assertEquals(bookingDtoOutput.getId(), bookingDtoOutputList.get(0).getId());
        assertEquals(bookingDtoOutput.getItem().getId(), bookingDtoOutputList.get(0).getItem().getId());
        assertEquals(bookingDtoOutput.getItem().getName(), bookingDtoOutputList.get(0).getItem().getName());
        assertEquals(bookingDtoOutput.getStart(), bookingDtoOutputList.get(0).getStart());
        assertEquals(bookingDtoOutput.getEnd(), bookingDtoOutputList.get(0).getEnd());
        assertEquals(bookingDtoOutput.getBooker().getId(), bookingDtoOutputList.get(0).getBooker().getId());
    }

    @Test
    void getAllByBookerTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());
        List<BookingDtoOutput> bookingDtoOutputList = bookingService
                .getAllByBooker(bookerNew.getId(), "ALL", 0, 1);


        assertEquals(bookingDtoOutput.getId(), bookingDtoOutputList.get(0).getId());
        assertEquals(bookingDtoOutput.getStart(), bookingDtoOutputList.get(0).getStart());
        assertEquals(bookingDtoOutput.getEnd(), bookingDtoOutputList.get(0).getEnd());
        assertEquals(bookingDtoOutput.getItem().getId(), bookingDtoOutputList.get(0).getItem().getId());
        assertEquals(bookingDtoOutput.getItem().getName(), bookingDtoOutputList.get(0).getItem().getName());
        assertEquals(bookingDtoOutput.getBooker().getId(), bookingDtoOutputList.get(0).getBooker().getId());
    }

    @Test
    void getByIdForBookingTest() {
        BookingDtoOutput bookingDtoOutput = bookingService.create(bookingDtoInput, bookerNew.getId());
        Booking booking = bookingService.getByIdForBooking(bookingDtoOutput.getId());
        assertEquals(bookingDtoOutput.getId(), booking.getId());
        assertEquals(bookingDtoOutput.getStart(), booking.getStart());
        assertEquals(bookingDtoOutput.getEnd(), booking.getEnd());
        assertEquals(bookingDtoOutput.getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoOutput.getItem().getName(), booking.getItem().getName());
        assertEquals(bookingDtoOutput.getBooker().getId(), booking.getBooker().getId());
    }
}