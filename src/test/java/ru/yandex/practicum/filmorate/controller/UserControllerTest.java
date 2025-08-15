package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.filmorate.model.User;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

@WebMvcTest(UserController.class)
public class UserControllerTest extends AbstractModelControllerTest<User> {
    @Override
    protected String getRootPath() {
        return "/users";
    }

    protected User jsonToModel(String json) throws JsonProcessingException, ParseException {
        LinkedHashMap<String, Object> attributes = new ObjectMapper().readValue(json, LinkedHashMap.class);
        return User.builder()
                .id(Long.valueOf(attributes.get("id").toString()))
                .email(attributes.get("email").toString())
                .login(attributes.get("login").toString())
                .name(attributes.get("name").toString())
                .birthday(parseDate(
                        attributes.get("birthday").toString()
                ))
                .build();
    }

    protected User makeModel() {
        return User.builder()
                .email(faker.internet().emailAddress())
                .login(faker.lorem().word())
                .name(faker.company().name())
                .birthday(Date.from(Instant.now()))
                .build();
    }

    protected ArrayList<User> makeInvalidModels() throws ParseException {
        ArrayList<User> models = new ArrayList<>();
        models.add(
                makeModel().toBuilder()
                        .email("")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .email("invalid-email")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .email(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login("")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login("with space")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .login(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .birthday(parseDate("2030-01-01"))
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .birthday(null)
                        .build()
        );

        return models;
    }
}
