package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class BookingDtoInputTest {
    @Autowired
    private JacksonTester<BookingDtoInput> jsonInput;
    @Autowired
    private JacksonTester<BookingDtoOutput> jsonOutput;

    @Test
    void testItemDto() throws Exception {
        BookingDtoOutput bookingDtoOutput = BookingDtoOutput.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .end(LocalDateTime.of(2022, 12, 10, 8, 0, 1))
                .status(Status.APPROVED)
                .item(new BookingDtoOutput.Item(1L, "Мяч"))
                .booker(new BookingDtoOutput.Booker(1L))
                .build();

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
    void testItemDtoFromJson() throws Exception {
        BookingDtoOutput bookingDtoOutput = BookingDtoOutput.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .end(LocalDateTime.of(2022, 12, 10, 8, 0, 1))
                .status(Status.APPROVED)
                .item(new BookingDtoOutput.Item(1L, "Мяч"))
                .booker(new BookingDtoOutput.Booker(1L))
                .build();

        JsonContent<BookingDtoOutput> result = jsonOutput.write(bookingDtoOutput);

        ObjectContent<BookingDtoInput> bookingDtoObjectContent = jsonInput.parse(result.getJson());

        assertEquals(bookingDtoOutput.getId(), bookingDtoObjectContent.getObject().getId());
        assertEquals(bookingDtoOutput.getStart(), bookingDtoObjectContent.getObject().getStart());
        assertEquals(bookingDtoOutput.getEnd(), bookingDtoObjectContent.getObject().getEnd());
    }
}