package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemDtoInputTest {
    @Autowired
    private JacksonTester<ItemDtoOutput> jsonOutput;
    @Autowired
    private JacksonTester<ItemDtoInput> jsonInput;

    @Test
    void testItemDtoFromJson() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemDtoOutput.builder()
                .id(1L)
                .name("Мяч")
                .description("Мяч для тенниса")
                .available(true)
                .requestId(1L)
                .comments(List.of())
                .build();

        JsonContent<ItemDtoOutput> result = jsonOutput.write(itemDtoOutput);
        ObjectContent<ItemDtoInput> itemDtoObjectContent = jsonInput.parse(result.getJson());

        assertEquals(itemDtoOutput.getId(), itemDtoObjectContent.getObject().getId());
        assertEquals(itemDtoOutput.getName(), itemDtoObjectContent.getObject().getName());
        assertEquals(itemDtoOutput.getDescription(), itemDtoObjectContent.getObject().getDescription());
        assertEquals(itemDtoOutput.getAvailable(), itemDtoObjectContent.getObject().getAvailable());
        assertEquals(itemDtoOutput.getRequestId(), itemDtoObjectContent.getObject().getRequestId());
    }
}