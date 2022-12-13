package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemRequestDtoOutputTest {

    @Autowired
    private JacksonTester<ItemRequestDtoOutput> jsonOutput;
    @Autowired
    private JacksonTester<List<ItemRequestDtoOutput>> jsonList;

    @Test
    void testItemDto() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = ItemRequestDtoOutput.builder()
                .id(1L)
                .description("Нужен мяч для тенниса")
                .items(List.of())
                .created(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .build();

        JsonContent<ItemRequestDtoOutput> result = jsonOutput.write(itemRequestDtoOutput);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Нужен мяч для тенниса");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDtoOutput.getCreated().toString());

    }

    @Test
    void testItemDtoList() throws Exception {
        List<ItemRequestDtoOutput> itemRequestDtoOutputList = new ArrayList<>();
        ItemRequestDtoOutput.Item item = new ItemRequestDtoOutput.Item();
        item.setId(1L);
        item.setRequestId(1L);
        item.setAvailable(true);
        item.setName("Мяч");
        item.setDescription("Мяч для тенниса");
        List<ItemRequestDtoOutput.Item> items = new ArrayList<>();
        items.add(item);
        ItemRequestDtoOutput itemRequestDtoOutput = new ItemRequestDtoOutput();
        itemRequestDtoOutput.setId(1L);
        itemRequestDtoOutput.setDescription("Нужен мяч для тенниса");
        itemRequestDtoOutput.setCreated(LocalDateTime
                .of(2022, 12, 8, 8, 0, 1));
        itemRequestDtoOutput.setItems(items);
        itemRequestDtoOutputList.add(itemRequestDtoOutput);

        JsonContent<List<ItemRequestDtoOutput>> result = jsonList.write(itemRequestDtoOutputList);

        assertThat(result).extractingJsonPathNumberValue("$[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].description")
                .isEqualTo("Нужен мяч для тенниса");
        assertThat(result).extractingJsonPathNumberValue("$[0].items.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].items.[0].name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$[0].items.[0].description")
                .isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$[0].items.[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$[0].items.[0].requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].created")
                .isEqualTo(itemRequestDtoOutput.getCreated().toString());

    }
}