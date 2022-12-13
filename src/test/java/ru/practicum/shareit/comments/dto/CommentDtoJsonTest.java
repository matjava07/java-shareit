package ru.practicum.shareit.comments.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testUserDto() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Приколдес");
        commentDto.setAuthorName("Чича");
        commentDto.setCreated(LocalDateTime.of(2022, 12, 8, 8, 0, 1));

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Приколдес");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Чича");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(commentDto.getCreated().toString());
    }

    @Test
    void testUserDtoFromJson() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Приколдес");
        commentDto.setAuthorName("Чича");
        commentDto.setCreated(LocalDateTime.of(2022, 12, 8, 8, 0, 1));

        JsonContent<CommentDto> result = json.write(commentDto);
        ObjectContent<CommentDto> userDtoObjectContent = json.parse(result.getJson());

        assertEquals(1L, userDtoObjectContent.getObject().getId());
        assertEquals("Приколдес", userDtoObjectContent.getObject().getText());
        assertEquals("Чича", userDtoObjectContent.getObject().getAuthorName());
        assertEquals(commentDto.getCreated(), userDtoObjectContent.getObject().getCreated());
    }
}