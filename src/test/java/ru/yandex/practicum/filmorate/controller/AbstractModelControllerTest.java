package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractModelControllerTest<M extends AbstractModel> {
    protected final Faker faker = new Faker();
    @Autowired
    protected MockMvc mvc;

    @Test
    public void shouldGetAll() throws Exception {
        mvc.perform(get(getRootPath()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreate() throws Exception {
        mvc.perform(
                        post(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(makeModel()))

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleEmptyCreation() throws Exception {
        mvc.perform(
                        post(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldValidateCreation() throws Exception {
        for (M invalidModel : this.makeInvalidModels()) {
            mvc.perform(
                            post(getRootPath())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(invalidModel))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void shouldUpdate() throws Exception {
        M model = createModel();
        M update = (M) makeModel();
        update.setId(model.getId());

        mvc.perform(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(update))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void shouldHandleEmptyUpdating() throws Exception {
        mvc.perform(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldValidateUpdating() throws Exception {
        M model = createModel();

        for (M invalidModel : this.makeInvalidModels()) {
            invalidModel.setId(model.getId());
            mvc.perform(
                            put(getRootPath())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(invalidModel))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    protected M createModel() throws Exception {
        return jsonToModel(
                mvc.perform(
                                post(getRootPath())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(makeModel()))

                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );
    }

    protected abstract M jsonToModel(String json) throws JsonProcessingException, ParseException;

    protected abstract M makeModel();

    protected abstract String getRootPath();

    protected abstract ArrayList<M> makeInvalidModels() throws ParseException;

    protected String toJson(Object data) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(data);
    }

    protected Date parseDate(String dateStr) throws ParseException {
        return Date.from(
                new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).toInstant()
        );
    }

    protected void assertEqualModels(M modelA, M modelB) throws JsonProcessingException {
        assertEquals(
                toJson(modelA),
                toJson(modelB)
        );
    }
}
