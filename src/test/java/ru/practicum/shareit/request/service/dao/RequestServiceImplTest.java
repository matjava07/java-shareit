package ru.practicum.shareit.request.service.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.dal.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private RequestServiceImpl requestService;
    private User requestor;
    private Item item;
    private ItemRequest request;
    private ItemRequestDtoInput itemRequestDtoInput;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setId(1L);
        requestor.setName("Чича");
        requestor.setEmail("koti@yandex.ru");

        User owner = new User();
        owner.setId(2L);
        owner.setName("Mot");
        owner.setEmail("mot@yandex.ru");

        request = new ItemRequest();
        request.setId(1L);
        request.setCreated(LocalDateTime.of(2022, 12, 7, 8, 0));
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
    void createTest() {
        Mockito
                .when(userService.getById(anyLong()))
                .thenReturn(requestor);
        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(request);

        ItemRequestDtoOutput itemRequestDtoOutput = requestService.create(itemRequestDtoInput, requestor.getId());
        Assertions.assertEquals(request.getId(), itemRequestDtoOutput.getId());
        Assertions.assertEquals(request.getCreated(), itemRequestDtoOutput.getCreated());
        Assertions.assertEquals(request.getDescriptionRequest(), itemRequestDtoOutput.getDescription());
    }

    @Test
    void getByIdTest() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(request);
        List<Item> items = new ArrayList<>();
        items.add(item);

        Mockito
                .when(userService.getById(anyLong()))
                .thenReturn(requestor);
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        Mockito
                .when(itemRepository.getByRequestId(itemRequests))
                .thenReturn(items);

        ItemRequestDtoOutput itemRequestDtoOutput = requestService.getById(request.getId(), requestor.getId());
        Assertions.assertEquals(request.getId(), itemRequestDtoOutput.getId());
        Assertions.assertEquals(request.getCreated(), itemRequestDtoOutput.getCreated());
        Assertions.assertEquals(request.getDescriptionRequest(), itemRequestDtoOutput.getDescription());
        Assertions.assertEquals(item.getId(), itemRequestDtoOutput.getItems().get(0).getId());
        Assertions.assertEquals(item.getRequest().getId(), itemRequestDtoOutput.getItems().get(0).getRequestId());
        Assertions.assertEquals(item.getName(), itemRequestDtoOutput.getItems().get(0).getName());
        Assertions.assertEquals(item.getAvailable(), itemRequestDtoOutput.getItems().get(0).getAvailable());
    }

    @Test
    void getAllByUserIdTest() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(request);
        Mockito
                .when(userService.getById(anyLong()))
                .thenReturn(requestor);
        Mockito
                .when(requestRepository.getAllByUser(requestor.getId(), Sort.by(DESC, "created")))
                .thenReturn(itemRequests);

        List<ItemRequestDtoOutput> itemRequestDtoOutputList = requestService.getAllByUserId(requestor.getId());
        Assertions.assertEquals(request.getId(), itemRequestDtoOutputList.get(0).getId());
        Assertions.assertEquals(request.getCreated(), itemRequestDtoOutputList.get(0).getCreated());
        Assertions.assertEquals(request.getDescriptionRequest(), itemRequestDtoOutputList.get(0).getDescription());
    }

    @Test
    void getAllTest() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(request);
        Mockito
                .when(userService.getById(anyLong()))
                .thenReturn(requestor);
        Mockito
                .when(requestRepository.getAllWithSize(requestor.getId(),
                        PageRequest.of(0, 1, Sort.by(DESC, "created"))))
                .thenReturn(itemRequests);
        List<ItemRequestDtoOutput> itemRequestDtoOutputList = requestService.getAll(requestor.getId(), 0, 1);
        Assertions.assertEquals(request.getId(), itemRequestDtoOutputList.get(0).getId());
        Assertions.assertEquals(request.getCreated(), itemRequestDtoOutputList.get(0).getCreated());
        Assertions.assertEquals(request.getDescriptionRequest(), itemRequestDtoOutputList.get(0).getDescription());
    }
}