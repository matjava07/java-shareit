package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoOutputTest {
    @Autowired
    private JacksonTester<ItemDtoOutput> jsonOutput;
    @Autowired
    private JacksonTester<List<ItemDtoOutput>> jsonList;
    @Test
    void testItemDto() throws Exception {
        ItemDtoOutput itemDtoOutput = ItemDtoOutput.builder()
                .id(1L)
                .name("Мяч")
                .description("Мяч для тенниса")
                .available(true)
                .requestId(1L)
                .comments(List.of())
                .build();

        JsonContent<ItemDtoOutput> result = jsonOutput.write(itemDtoOutput);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Мяч");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Мяч для тенниса");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(List.of());
    }

    @Test
    void testItemDtoList() throws Exception {
        List<ItemDtoOutput> itemDtoOutputList = new ArrayList<>();

        ItemDtoOutput.Comment comment = new ItemDtoOutput.Comment(1L, "классный мяч", "Кот",
                LocalDateTime.of(2022, 12, 10, 8, 0, 1));
        List<ItemDtoOutput.Comment> comments = new ArrayList<>();
        comments.add(comment);

        ItemDtoOutput.Booking bookingLast = new ItemDtoOutput.Booking();
        bookingLast.setId(1L);
        bookingLast.setBookerId(1L);

        ItemDtoOutput.Booking bookingNext = new ItemDtoOutput.Booking();
        bookingNext.setId(2L);
        bookingNext.setBookerId(2L);

        ItemDtoOutput itemDtoOutput = new ItemDtoOutput(1L, "Мяч", "Мяч для тенниса", true);
        itemDtoOutput.setRequestId(1L);
        itemDtoOutput.setComments(comments);
        itemDtoOutput.setLastBooking(bookingLast);
        itemDtoOutput.setNextBooking(bookingNext);
        itemDtoOutputList.add(itemDtoOutput);

        JsonContent<List<ItemDtoOutput>> result = jsonList.write(itemDtoOutputList);

        assertThat(result).extractingJsonPathNumberValue("$[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].name").isEqualTo("Мяч");
        assertThat(result).extractingJsonPathStringValue("$[0].description")
                .isEqualTo("Мяч для тенниса");
        assertThat(result).extractingJsonPathBooleanValue("$[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$[0].requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$[0].comments.[0].id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].comments.[0].text")
                .isEqualTo(comments.get(0).getText());
        assertThat(result).extractingJsonPathStringValue("$[0].comments.[0].authorName")
                .isEqualTo(comments.get(0).getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$[0].comments.[0].created")
                .isEqualTo(comments.get(0).getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$[0].lastBooking.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$[0].lastBooking.bookerId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$[0].nextBooking.id")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$[0].nextBooking.bookerId")
                .isEqualTo(2);
    }

}