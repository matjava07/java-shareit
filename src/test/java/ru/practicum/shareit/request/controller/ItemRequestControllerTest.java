package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.dal.RequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private RequestService requestService;
    @Autowired
    private MockMvc mvc;
    private User owner;
    private User requestor;
    private Item item;
    private ItemRequest request;
    private ItemRequestDtoInput itemRequestDtoInput;
    public static final String USER_ID = "X-Sharer-User-Id";

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

        request = new ItemRequest();
        request.setId(1L);
        request.setCreated(LocalDateTime.of(2022, 12, 7, 8, 0, 1));
        request.setDescriptionRequest("Хочу теннисный мячик");
        request.setRequestor(requestor);

        item = new Item();
        item.setId(1L);
        item.setName("Мячик");
        item.setAvailable(true);
        item.setDescription("Теннисный мячик");
        item.setRequest(request);

        itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setId(request.getId());
        itemRequestDtoInput.setDescription(request.getDescriptionRequest());
        itemRequestDtoInput.setCreated(request.getCreated());
    }

    @Test
    void createTest() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = RequestMapper.toRequestDto(request);
        Mockito
                .when(requestService.create(itemRequestDtoInput, requestor.getId()))
                .thenReturn(itemRequestDtoOutput);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoInput))
                        .header(USER_ID, requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequestDtoOutput.getCreated().toString())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOutput.getDescription())));
    }

    @Test
    void getAllByUserIdTest() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = RequestMapper.toRequestDto(request);
        ItemRequestDtoOutput.Item item1 = new ItemRequestDtoOutput.Item();
        item1.setId(item.getId());
        item1.setRequestId(item.getRequest().getId());
        item1.setAvailable(item.getAvailable());
        item1.setName(item.getName());
        item1.setDescription(item.getDescription());
        List<ItemRequestDtoOutput.Item> items = new ArrayList<>();
        items.add(item1);
        itemRequestDtoOutput.setItems(items);

        List<ItemRequestDtoOutput> itemRequestDtoOutputList = new ArrayList<>();
        itemRequestDtoOutputList.add(itemRequestDtoOutput);
        Mockito
                .when(requestService.getAllByUserId(requestor.getId()))
                .thenReturn(itemRequestDtoOutputList);

        mvc.perform(get("/requests")
                        .header(USER_ID, requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(itemRequestDtoOutput.getCreated().toString())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOutput.getDescription())))
                .andExpect(jsonPath("$[0].items.[0].id",
                        is(itemRequestDtoOutput.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items.[0].name",
                        is(itemRequestDtoOutput.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items.[0].description",
                        is(itemRequestDtoOutput.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items.[0].available",
                        is(itemRequestDtoOutput.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items.[0].requestId",
                        is(itemRequestDtoOutput.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    void getAllTest() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = RequestMapper.toRequestDto(request);
        ItemRequestDtoOutput.Item item1 = new ItemRequestDtoOutput.Item();
        item1.setId(item.getId());
        item1.setRequestId(item.getRequest().getId());
        item1.setAvailable(item.getAvailable());
        item1.setName(item.getName());
        item1.setDescription(item.getDescription());
        List<ItemRequestDtoOutput.Item> items = new ArrayList<>();
        items.add(item1);
        itemRequestDtoOutput.setItems(items);

        List<ItemRequestDtoOutput> itemRequestDtoOutputList = new ArrayList<>();
        itemRequestDtoOutputList.add(itemRequestDtoOutput);
        Mockito
                .when(requestService.getAll(owner.getId(), 0, 1))
                .thenReturn(itemRequestDtoOutputList);

        mvc.perform(get("/requests/all")
                        .header(USER_ID, owner.getId())
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(itemRequestDtoOutput.getCreated().toString())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoOutput.getDescription())))
                .andExpect(jsonPath("$[0].items.[0].id",
                        is(itemRequestDtoOutput.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items.[0].name",
                        is(itemRequestDtoOutput.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items.[0].description",
                        is(itemRequestDtoOutput.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items.[0].available",
                        is(itemRequestDtoOutput.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items.[0].requestId",
                        is(itemRequestDtoOutput.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    void getAllFailTest() throws Exception {
        mvc.perform(get("/requests/all")
                        .header(USER_ID, owner.getId())
                        .param("from", "-1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdTest() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = RequestMapper.toRequestDto(request);
        ItemRequestDtoOutput.Item item1 = new ItemRequestDtoOutput.Item();
        item1.setId(item.getId());
        item1.setRequestId(item.getRequest().getId());
        item1.setAvailable(item.getAvailable());
        item1.setName(item.getName());
        item1.setDescription(item.getDescription());
        List<ItemRequestDtoOutput.Item> items = new ArrayList<>();
        items.add(item1);
        itemRequestDtoOutput.setItems(items);

        Mockito
                .when(requestService.getById(request.getId(), requestor.getId()))
                .thenReturn(itemRequestDtoOutput);

        mvc.perform(get("/requests/1")
                        .header(USER_ID, requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequestDtoOutput.getCreated().toString())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoOutput.getDescription())))
                .andExpect(jsonPath("$.items.[0].id",
                        is(itemRequestDtoOutput.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.items.[0].name",
                        is(itemRequestDtoOutput.getItems().get(0).getName())))
                .andExpect(jsonPath("$.items.[0].description",
                        is(itemRequestDtoOutput.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$.items.[0].available",
                        is(itemRequestDtoOutput.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$.items.[0].requestId",
                        is(itemRequestDtoOutput.getItems().get(0).getRequestId()), Long.class));
    }
}