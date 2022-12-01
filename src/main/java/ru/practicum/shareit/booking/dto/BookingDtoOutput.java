package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoOutput {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status = Status.WAITING;

    private Booker booker;

    private Item item;

    @Data
    @AllArgsConstructor
    public static class Booker {
        private long id;
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private long id;
        private String name;
    }
}
