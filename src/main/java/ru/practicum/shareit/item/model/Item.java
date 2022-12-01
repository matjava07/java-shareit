package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;

    public static class Builder {

        private final Item item;

        public Builder() {
            item = new Item();
        }

        public Builder setId(Long id) {
            item.id = id;
            return this;
        }

        public Builder setName(String name) {
            item.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            item.description = description;
            return this;
        }

        public Builder setAvailable(Boolean available) {
            item.available = available;
            return this;
        }

        public Builder setOwner(User owner) {
            item.owner = owner;
            return this;
        }

        public Builder setRequest(Long request) {
            item.request = request;
            return this;
        }

        public Item build() {
            return item;
        }
    }
}
