package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.factory.Factory;
import ru.yandex.practicum.filmorate.model.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ModelControllerTest<M extends Model> {
    protected final Faker faker = new Faker();
    @Autowired
    protected MockMvc mvc;

    /**
     * @see ModelController#getAll()
     */
    @Test
    public void shouldGetAll() throws Exception {
        mvc.perform(get(getRootPath()))
                .andExpect(status().isOk());
    }

    /**
     * @see ModelController#create(Model)
     */
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

    /**
     * @see ModelController#create(Model
     */
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

    /**
     * @see ModelController#update(Model)
     */
    @Test
    public void shouldUpdate() throws Exception {
        M model = createModel();
        M update = makeModel();
        update.setId(model.getId());

        mvc.perform(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(update))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * @see ModelController#update(Model)
     */
    @Test
    public void shouldHandleEmptyUpdating() throws Exception {
        mvc.perform(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * @see ModelController#update(Model)
     */
    @Test
    public void shouldHandleNotExistedUpdating() throws Exception {
        M update = makeModel();
        update.setId(Math.round(Math.pow(10L, 10)));

        mvc.perform(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(update))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * @see ModelController#update(Model)
     */
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

    protected M jsonToModel(String json) throws JsonProcessingException, ParseException {
        return (M) attributesToModel(
                new ObjectMapper()
                        .readValue(json, LinkedHashMap.class)
        );
    }

    protected List<M> jsonToModels(String json) throws JsonProcessingException {
        return new ObjectMapper()
                .readValue(json, ArrayList.class)
                .stream()
                .map((attributes) -> {
                    try {
                        return attributesToModel((LinkedHashMap<String, Object>) attributes);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    protected M makeModel() {
        return getModelFactory()
                .make();
    }

    protected abstract M attributesToModel(LinkedHashMap<String, Object> attributes) throws ParseException;

    protected abstract Factory<M> getModelFactory();

    protected abstract String getRootPath();

    protected abstract ArrayList<M> makeInvalidModels() throws ParseException;

    protected String toJson(Object data) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(data);
    }

    protected Date parseDate(String dateStr) throws ParseException {
        return Date.from(
                new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).toInstant()
        );
    }

    protected void assertEqualModels(M expectedModel, M actualModel) throws JsonProcessingException {
        assertEquals(
                toJson(expectedModel),
                toJson(actualModel)
        );
    }
}
