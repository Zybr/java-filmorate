package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends AbstractModelControllerTest<Film> {
    @Override
    protected String getRootPath() {
        return "/films";
    }

    protected Film jsonToModel(String json) throws JsonProcessingException, ParseException {
        LinkedHashMap<String, Object> attributes = new ObjectMapper().readValue(json, LinkedHashMap.class);
        return Film.builder()
                .id(Long.valueOf(attributes.get("id").toString()))
                .name(attributes.get("name").toString())
                .description(attributes.get("description").toString())
                .releaseDate(parseDate(
                        attributes.get("releaseDate").toString()
                ))
                .duration(Integer.parseInt(attributes.get("duration").toString()))
                .build();
    }

    protected Film makeModel() {
        return Film.builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .releaseDate(Date.from(Instant.now()))
                .duration(faker.random().nextInt(10, 100))
                .build();
    }

    protected ArrayList<Film> makeInvalidModels() throws ParseException {
        ArrayList<Film> models = new ArrayList<>();
        models.add(
                makeModel().toBuilder()
                        .name("")
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .name(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .description(faker.lorem().sentences(50).toString())
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .description(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .duration(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .duration(-1)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .releaseDate(null)
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .releaseDate(parseDate("1895-12-01"))
                        .build()
        );
        models.add(
                makeModel().toBuilder()
                        .releaseDate(parseDate("2030-12-01"))
                        .build()
        );

        return models;
    }
}
