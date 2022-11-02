package ru.practicum.shareit.generate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GenerateId {
    private Long id = Long.valueOf(1);

    public Long getId() {
        return id++;
    }
}
