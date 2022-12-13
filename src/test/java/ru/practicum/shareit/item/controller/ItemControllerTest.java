package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.mapper.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.service.dal.CommentService;
import ru.practicum.shareit.item.dto.ItemDtoInput;
import ru.practicum.shareit.item.dto.ItemDtoOutput;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.dal.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @MockBean
    CommentService commentService;
    @Autowired
    private MockMvc mvc;
    private User owner;
    private User requestor;
    private Item item;
    private ItemDtoInput itemDtoInput;
    private Comment comment;
    private Booking booking;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setId(1L);
        requestor.setName("Чича");
        requestor.setEmail("koti@yandex.ru");

        owner = new User();
        owner.setId(2L);
        owner.setName("Mot");
        owner.setEmail("mot@yandex.ru");

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setCreated(LocalDateTime.of(2022, 12, 7, 8, 0));
        request.setDescriptionRequest("Хочу теннисный мячик");
        request.setRequestor(requestor);

        item = new Item();
        item.setId(1L);
        item.setName("Мячик");
        item.setAvailable(true);
        item.setDescription("Теннисный мячик");

        itemDtoInput = new ItemDtoInput();
        itemDtoInput.setId(item.getId());
        itemDtoInput.setName(item.getName());
        itemDtoInput.setDescription(item.getDescription());
        itemDtoInput.setAvailable(item.getAvailable());
        itemDtoInput.setRequestId(request.getId());

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(requestor);
        comment.setText("Прикалдесный шар");
        comment.setCreated(LocalDateTime.of(2022, 12, 10, 9, 0, 1));
        comment.setItem(item);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2022, 12, 8, 8, 0, 1));
        booking.setEnd(LocalDateTime.of(2022, 12, 10, 8, 0, 1));
        booking.setStatus(Status.WAITING);
        booking.setBooker(requestor);
        booking.setItem(item);
    }

    @Test
    void create() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemMapper.toItemDto(item);
        Mockito
                .when(itemService.create(itemDtoInput, owner.getId()))
                .thenReturn(itemDtoOutput);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoInput))
                        .header("X-Sharer-User-Id", owner.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOutput.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOutput.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDtoOutput.getRequestId())))
                .andExpect(jsonPath("$.available", is(itemDtoOutput.getAvailable())));
    }

    @Test
    void update() throws Exception {
        itemDtoInput.setName("Шар");
        ItemDtoOutput itemDtoOutput = ItemMapper.toItemDto(item);
        itemDtoOutput.setName("Шар");
        Mockito
                .when(itemService.update(itemDtoInput, owner.getId()))
                .thenReturn(itemDtoOutput);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoInput))
                        .header("X-Sharer-User-Id", owner.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOutput.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOutput.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDtoOutput.getRequestId())))
                .andExpect(jsonPath("$.available", is(itemDtoOutput.getAvailable())));
    }

    @Test
    void getById() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemMapper.toItemDto(item);
        ItemDtoOutput.Booking lastBooking = new ItemDtoOutput.Booking();
        lastBooking.setId(booking.getId());
        lastBooking.setBookerId(requestor.getId());
        itemDtoOutput.setLastBooking(lastBooking);
        ItemDtoOutput.Comment comment1 = new ItemDtoOutput.Comment(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
        List<ItemDtoOutput.Comment> comments = new ArrayList<>();
        comments.add(comment1);
        itemDtoOutput.setComments(comments);
        Mockito
                .when(itemService.getById(item.getId(), owner.getId()))
                .thenReturn(itemDtoOutput);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", owner.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoOutput.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOutput.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDtoOutput.getRequestId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDtoOutput.getAvailable())))
                .andExpect(jsonPath("$.comments.[0].id",
                        is(itemDtoOutput.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.comments.[0].authorName",
                        is(itemDtoOutput.getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.comments.[0].text", is(itemDtoOutput.getComments().get(0).getText())))
                .andExpect(jsonPath("$.comments.[0].created",
                        is(itemDtoOutput.getComments().get(0).getCreated().toString())))
                .andExpect(jsonPath("$.lastBooking.id",
                        is(itemDtoOutput.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId",
                        is(itemDtoOutput.getLastBooking().getBookerId()), Long.class));
    }

    @Test
    void getAll() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemMapper.toItemDto(item);
        ItemDtoOutput.Booking lastBooking = new ItemDtoOutput.Booking();
        lastBooking.setId(booking.getId());
        lastBooking.setBookerId(requestor.getId());
        itemDtoOutput.setLastBooking(lastBooking);
        ItemDtoOutput.Comment comment1 = new ItemDtoOutput.Comment(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
        List<ItemDtoOutput.Comment> comments = new ArrayList<>();
        comments.add(comment1);
        itemDtoOutput.setComments(comments);
        List<ItemDtoOutput> itemDtoOutputList = new ArrayList<>();
        itemDtoOutputList.add(itemDtoOutput);
        Mockito
                .when(itemService.getAll(owner.getId(), 0, 1))
                .thenReturn(itemDtoOutputList);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOutput.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoOutput.getDescription())))
                .andExpect(jsonPath("$[0].requestId", is(itemDtoOutput.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoOutput.getAvailable())))
                .andExpect(jsonPath("$[0].comments.[0].id",
                        is(itemDtoOutput.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].comments.[0].authorName",
                        is(itemDtoOutput.getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].comments.[0].text", is(itemDtoOutput.getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].comments.[0].created",
                        is(itemDtoOutput.getComments().get(0).getCreated().toString())))
                .andExpect(jsonPath("$[0].lastBooking.id",
                        is(itemDtoOutput.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId",
                        is(itemDtoOutput.getLastBooking().getBookerId()), Long.class));
    }

    @Test
    void getByText() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemMapper.toItemDto(item);
        ItemDtoOutput.Booking lastBooking = new ItemDtoOutput.Booking();
        lastBooking.setId(booking.getId());
        lastBooking.setBookerId(requestor.getId());
        itemDtoOutput.setLastBooking(lastBooking);
        ItemDtoOutput.Comment comment1 = new ItemDtoOutput.Comment(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
        List<ItemDtoOutput.Comment> comments = new ArrayList<>();
        comments.add(comment1);
        itemDtoOutput.setComments(comments);
        List<ItemDtoOutput> itemDtoOutputList = new ArrayList<>();
        itemDtoOutputList.add(itemDtoOutput);
        Mockito
                .when(itemService.getByText("мяч", 0, 1))
                .thenReturn(itemDtoOutputList);

        mvc.perform(get("/items/search")
                        .param("text", "мяч")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoOutput.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoOutput.getDescription())))
                .andExpect(jsonPath("$[0].requestId", is(itemDtoOutput.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoOutput.getAvailable())))
                .andExpect(jsonPath("$[0].comments.[0].id",
                        is(itemDtoOutput.getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].comments.[0].authorName",
                        is(itemDtoOutput.getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].comments.[0].text", is(itemDtoOutput.getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].comments.[0].created",
                        is(itemDtoOutput.getComments().get(0).getCreated().toString())))
                .andExpect(jsonPath("$[0].lastBooking.id",
                        is(itemDtoOutput.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId",
                        is(itemDtoOutput.getLastBooking().getBookerId()), Long.class));
    }

    @Test
    void getByWithoutText() throws Exception {
        Mockito
                .when(itemService.getByText("", 0, 1))
                .thenReturn(List.of());

        mvc.perform(get("/items/search")
                        .param("text", "")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.of()), List.class));
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        Mockito
                .when(commentService.create(commentDto, item.getId(), requestor.getId()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())));
    }
}