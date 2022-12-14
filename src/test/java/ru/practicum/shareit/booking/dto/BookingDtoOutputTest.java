package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoOutputTest {
    @Autowired
    private JacksonTester<BookingDtoOutput> jsonOutput;
    @Autowired
    private JacksonTester<List<BookingDtoOutput>> jsonList;

    @Test
    void testItemDto() throws Exception {
        BookingDtoOutput bookingDtoOutput = new BookingDtoOutput();
        bookingDtoOutput.setId(1L);
        bookingDtoOutput.setStart(LocalDateTime.of(2022, 12, 8, 8, 0, 1));
        bookingDtoOutput.setEnd(LocalDateTime.of(2022, 12, 10, 8, 0, 1));
        bookingDtoOutput.setStatus(Status.APPROVED);
        bookingDtoOutput.setItem(new BookingDtoOutput.Item(1L, "Мяч"));
        bookingDtoOutput.setBooker(new BookingDtoOutput.Booker(1L));

        JsonContent<BookingDtoOutput> result = jsonOutput.write(bookingDtoOutput);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Мяч");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDtoOutput.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingDtoOutput.getEnd().toString());
    }

    @Test
    void testItemDtoFromJsonList() throws Exception {
        List<BookingDtoOutput> bookingDtoOutputList = new ArrayList<>();
        BookingDtoOutput bookingDtoOutput = BookingDtoOutput.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .end(LocalDateTime.of(2022, 12, 10, 8, 0, 1))
                .status(Status.APPROVED)
                .item(new BookingDtoOutput.Item(1L, "Мяч"))
                .booker(new BookingDtoOutput.Booker(1L))
                .build();
        bookingDtoOutputList.add(bookingDtoOutput);

        JsonContent<List<BookingDtoOutput>> result = jsonList.write(bookingDtoOutputList);

        assertThat(result).extractingJsonPathNumberValue("$[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$[0].item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].item.name").isEqualTo("Мяч");
        assertThat(result).extractingJsonPathStringValue("$[0].status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$[0].booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$[0].start")
                .isEqualTo(bookingDtoOutput.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$[0].end")
                .isEqualTo(bookingDtoOutput.getEnd().toString());
    }
}