package ru.practicum.shareit.comments.service.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private User booker;
    private Item item;
    private Booking booking;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setId(1L);
        booker.setName("Чича");
        booker.setEmail("koti@yandex.ru");

        User owner = new User();
        owner.setId(2L);
        owner.setName("Mot");
        owner.setEmail("mot@yandex.ru");

        item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setName("Мячик");
        item.setAvailable(true);
        item.setDescription("Теннисный мячик");

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2022, 12, 8, 8, 0));
        booking.setEnd(LocalDateTime.of(2022, 12, 9, 8, 0));
        booking.setStatus(Status.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.of(2022, 12, 9, 12, 0));
        comment.setText("Превосходный мяч");

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setAuthorName(booker.getName());
        commentDto.setCreated(LocalDateTime.of(2022, 12, 9, 12, 0));
        commentDto.setText("Превосходный мяч");
    }

    @Test
    void createTest() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        Mockito
                .when(itemService.getByIdForItem(anyLong()))
                .thenReturn(item);
        Mockito
                .when(bookingRepository.getAllByBookerPast(anyLong(),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(bookings);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDtoNew = commentService.create(commentDto, item.getId(), booker.getId());
        Assertions.assertEquals(comment.getId(), commentDtoNew.getId());
        Assertions.assertEquals(comment.getAuthor().getName(), commentDtoNew.getAuthorName());
        Assertions.assertEquals(comment.getText(), commentDtoNew.getText());
        Assertions.assertEquals(comment.getCreated(), commentDtoNew.getCreated());

    }
}