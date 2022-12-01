package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long request;

    public static class Builder {

        private final ItemDto itemDto;

        public Builder() {
            itemDto = new ItemDto();
        }

        public Builder setId(Long id) {
            itemDto.id = id;
            return this;
        }

        public Builder setName(String name) {
            itemDto.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            itemDto.description = description;
            return this;
        }

        public Builder setAvailable(Boolean available) {
            itemDto.available = available;
            return this;
        }

        public Builder setRequest(Long request) {
            itemDto.request = request;
            return this;
        }

        public ItemDto build() {
            return itemDto;
        }
    }
}
