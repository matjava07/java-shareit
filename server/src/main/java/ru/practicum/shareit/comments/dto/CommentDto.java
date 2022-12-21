package ru.practicum.shareit.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();
}
