package ru.practicum.shareit.user.service.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.dal.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTestIntegration {

    private final EntityManager em;
    private final UserService service;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("Чича");
        user1.setEmail("koti@yandex.ru");
    }

    @Test
    void create() {
        User userNew = service.create(user1);

        TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", userNew.getId())
                .getSingleResult();

        assertThat(user1.getId(), notNullValue());
        assertEquals(user1.getName(), user.getName());
        assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    void update() {
        User userNew = service.create(user1);
        User user2 = new User();
        user2.setId(userNew.getId());
        user2.setName("Mot");
        user2.setEmail("mot@yandex.ru");

        service.update(user2);

        TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", userNew.getId())
                .getSingleResult();

        assertEquals(user2.getId(), user.getId());
        assertEquals(user2.getName(), user.getName());
        assertEquals(user2.getEmail(), user.getEmail());
    }

    @Test
    void getAll() {

        User user2 = new User();
        user2.setName("Mot");
        user2.setEmail("mot@yandex.ru");

        em.persist(user1);

        User newUser1 = service.create(user1);
        User newUser2 = service.create(user2);

        List<User> allUsers = new ArrayList<>();
        allUsers.add(newUser1);
        allUsers.add(newUser2);

        List<User> targetUsers = service.getAll();

        assertEquals(allUsers.size(), targetUsers.size());
        for (User user : allUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", equalTo(user.getId())),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }


    }

    @Test
    void getById() {
        User user2 = new User();
        user2.setName("Mot");
        user2.setEmail("mot@yandex.ru");

        User userNew1 = service.create(user1);
        service.create(user2);

        User user = service.getById(userNew1.getId());

        assertEquals(user1.getId(), user.getId());
        assertEquals(user1.getName(), user.getName());
        assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    void deleteById() {
        User user2 = new User();
        user2.setName("Mot");
        user2.setEmail("mot@yandex.ru");

        User userNew1 = service.create(user1);
        service.create(user2);

        service.deleteById(userNew1.getId());
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> users = query.getResultList();

        assertEquals(1, users.size());
        assertEquals(user2.getId(), users.get(0).getId());
        assertEquals(user2.getName(), users.get(0).getName());
        assertEquals(user2.getEmail(), users.get(0).getEmail());
    }
}