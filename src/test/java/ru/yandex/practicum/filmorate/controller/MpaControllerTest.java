package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see MpaController
 */
@WebMvcTest(MpaControllerTest.class)
public class MpaControllerTest {
    @Autowired
    protected MockMvc mvc;

    /**
     * @see MpaController#getAll()
     */
    @Test
    public void shouldGetAll() throws Exception {
        mvc.perform(get("/mpa"))
                .andExpect(status().isOk());
    }

    /**
     * @see MpaController#getOne(Long)
     */
    @Test
    public void shouldGetOne() throws Exception {
        mvc.perform(get("/mpa/1"))
                .andExpect(status().isOk());
    }

    /**
     * @see MpaController#getOne(Long)
     */
    @Test
    public void shouldGetOneNotFound() throws Exception {
        mvc.perform(get("/mpa/1000"))
                .andExpect(status().isNotFound());
    }
}
