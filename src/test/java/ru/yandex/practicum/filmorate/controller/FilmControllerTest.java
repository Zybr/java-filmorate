package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private final Faker faker = new Faker();
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldGetAll() throws Exception {
        mvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreate() throws Exception {
        Film model = makeModelBuilder();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(model))

                )
                .andDo(print())
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void shouldValidateCreation() throws Exception {
        ArrayList<Film> models = new ArrayList<>();
        models.add(
                makeModelBuilder().toBuilder()
                        .name("")
                        .build()
        );
        models.add(
                makeModelBuilder().toBuilder()
                        .duration(-1)
                        .build()
        );
        models.add(
                makeModelBuilder().toBuilder()
                        .description(faker.lorem().sentences(50).toString())
                        .build()
        );
        models.add(
                makeModelBuilder().toBuilder()
                        .releaseDate("1895-12-01")
                        .build()
        );

        for (Film model : models) {
            mvc.perform(
                            post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(model))
                    )
                    .andDo(print())
                    .andExpect(
                            status().isBadRequest()
                    );
        }
    }

    private String toJson(Object data) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(data);
    }

    private Film makeModelBuilder() {
        return Film.builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .releaseDate(makeDateString())
                .duration(faker.random().nextInt(10, 100))
                .build();
    }

    private String makeDateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(
                Date.from(
                        faker.date().birthday().toInstant()
                )
        );
    }
}
