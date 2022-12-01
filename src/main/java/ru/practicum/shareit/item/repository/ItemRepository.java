package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.owner.id = ?1 " +
            " order by i.id")
    List<Item> getAll(Long id);



    @Query("select i from Item i where (lower(i.description) like concat('%', ?1, '%') " +
            "or lower(i.name) like concat('%', ?1, '%')) and i.available = true ")
    List<Item> getByText(String text);
}
