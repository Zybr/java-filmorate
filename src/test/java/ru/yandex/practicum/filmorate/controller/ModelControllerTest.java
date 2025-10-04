package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.yandex.practicum.filmorate.factory.Factory;
import ru.yandex.practicum.filmorate.model.Model;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ModelControllerTest<M extends Model> {
    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void shouldGetOne() throws Exception {
        M model = createModel();

        mvc.perform(get(
                        getRootPath() + "/" + model.getId()
                ))
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

    protected Map<String, Object> makeRequest(
            MockHttpServletRequestBuilder request
    ) throws Exception {
        return makeRequest(
                request,
                status().isOk()
        );
    }

    protected Map<String, Object> makeRequest(
            MockHttpServletRequestBuilder request,
            ResultMatcher statusCode
    ) throws Exception {
        MockHttpServletResponse response = mvc.perform(request)
                .andDo(print())
                .andExpect(statusCode)
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("utf-8");
        return fromJson(
                response.getContentAsString()
        );
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
                objectMapper
                        .readValue(json, LinkedHashMap.class)
        );
    }

    protected List<M> jsonToModels(String json) throws JsonProcessingException {
        return objectMapper
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
        return objectMapper
                .writeValueAsString(data);
    }

    protected Map<String, Object> fromJson(String json) throws JsonProcessingException {
        return objectMapper
                .readValue(
                        json,
                        new TypeReference<HashMap<String, Object>>() {
                        }
                );
    }

    protected LocalDate parseDate(String dateStr) throws ParseException {
        return LocalDate.parse(
                dateStr,
                DateTimeFormatter.ISO_LOCAL_DATE
        );
    }

    protected void assertEqualModels(M expectedModel, M actualModel) throws JsonProcessingException {
        assertEquals(
                toJson(expectedModel),
                toJson(actualModel)
        );
    }

    protected void assertSavedData(
            Map<String, Object> expectedData,
            Map<String, Object> savedData
    ) {
        this.assertSavedData(
                expectedData,
                savedData,
                List.of()
        );
    }

    protected void assertSavedData(
            Map<String, Object> expectedData,
            Map<String, Object> savedData,
            List<String> excludedKeys
    ) {
        expectedData = new HashMap<>(expectedData);
        savedData = new HashMap<>(savedData);

        Set<String> comparingKeys = expectedData.keySet();
        excludedKeys.forEach(comparingKeys::remove);

        if (
                expectedData.containsKey("id")
                        && expectedData.get("id") == null
        ) {
            comparingKeys.remove("id");
        }

        for (String key : savedData.keySet().stream().toList()) {
            if (!comparingKeys.contains(key)) {
                savedData.remove(key);
            }
        }

        Assertions.assertEquals(
                expectedData,
                savedData
        );
    }
}
