package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see GenreController
 */
@WebMvcTest(GenreControllerTest.class)
public class GenreControllerTest {
    @Autowired
    protected MockMvc mvc;

    /**
     * @see GenreController#getAll()
     */
    @Test
    public void shouldGetAll() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    /**
     * @see GenreController#getOne(Long)
     */
    @Test
    public void shouldGetOne() throws Exception {
        mvc.perform(get("/genres/1"))
                .andExpect(status().isOk());
    }

    /**
     * @see GenreController#getOne(Long)
     */
    @Test
    public void shouldGetOneNotFound() throws Exception {
        mvc.perform(get("/genres/1000"))
                .andExpect(status().isNotFound());
    }
}
