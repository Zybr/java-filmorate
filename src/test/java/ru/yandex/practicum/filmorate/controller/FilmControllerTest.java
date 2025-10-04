package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dto.request.FilmSavingDto;
import ru.yandex.practicum.filmorate.factory.FilmFactory;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.*;

import java.text.ParseException;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends ModelControllerTest<Film> {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private MpaStorage mpaStorage;
    @Autowired
    private CategoryStorage categoryStorage;
    @Autowired
    private GenreStorage genreStorage;
    @Autowired
    private UserStorage userStorage;

    @Autowired
    private FilmRowMapper filmRowMapper;

    @Autowired
    private FilmService filmService;

    private final FilmFactory filmFactory = new FilmFactory();
    private final UserFactory userFactory = new UserFactory();

    @BeforeEach
    public void initFactories() {
        userFactory.setStorage(userStorage);
        filmFactory
                .setStorage(filmStorage)
                .setMpaStorage(mpaStorage)
                .setCategoryStorage(categoryStorage)
                .setGenreStorage(genreStorage);
    }

    @Test
    public void shouldGetOne() throws Exception {
        FilmSavingDto filmData = getModelFactory().makeSavingDto();

        assertSavedFilm(
                fromJson(toJson(filmData)),
                makeRequest(
                        get(String.format(
                                getRootPath() + "/%d",
                                filmStorage.create(
                                        filmRowMapper.mapWithSaving(filmData)
                                ).getId()
                        ))
                )
        );
    }

    /**
     * @see FilmController#create(FilmSavingDto)
     */
    @Test
    public void shouldCreate() throws Exception {
        FilmSavingDto requestDto = getModelFactory().makeSavingDto();

        assertSavedFilm(
                fromJson(toJson(requestDto)),
                makeRequest(
                        post(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(requestDto))
                )
        );
    }

    /**
     * @see FilmController#update(FilmSavingDto)
     */
    @Test
    public void shouldUpdate() throws Exception {
        Film model = createModel();
        FilmSavingDto requestDto = getModelFactory().makeSavingDto();
        requestDto.setId(model.getId());

        assertSavedFilm(
                fromJson(toJson(requestDto)),
                makeRequest(
                        put(getRootPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(requestDto))
                )
        );
    }

    /**
     * @see FilmController#addLike(Long, Long)
     */
    @Test
    public void shouldAddLike() throws Exception {
        Film film = filmFactory.create();
        User user = userFactory.create();

        doAddLikeRequest(
                film.getId(),
                user.getId()
        );

        Assertions.assertTrue(
                refreshFilm(film)
                        .getLikes()
                        .contains(user.getId())
        );
    }

    /**
     * @see FilmController#removeLike(Long, Long)
     */
    @Test
    public void shouldRemoveLike() throws Exception {
        Film film = filmFactory.create();
        User user = userFactory.create();
        filmService.addLike(film, user);

        doRemoveLikeRequest(
                film.getId(),
                user.getId()
        );

        Assertions.assertFalse(
                refreshFilm(film)
                        .getLikes()
                        .contains(user.getId())
        );
    }

    /**
     * @see FilmController#getPopular(int)
     */
    @Test
    public void shouldGetPopular() throws Exception {
        userFactory.createList(12);
        filmFactory.createList(12);

        for (int i = 0; i < 50; i++) {
            filmService.addLike(
                    faker.options()
                            .nextElement(filmStorage.findAll()),
                    faker.options()
                            .nextElement(userStorage.findAll())
            );
        }

        this.assertIsTopRated(
                doGetPopularRequest(),
                10
        );
        this.assertIsTopRated(
                doGetPopularRequest(5),
                5
        );
    }

    @Test
    public void shouldHandleNotExisted() throws Exception {
        Long notExistedId = Math.round(Math.pow(10, 10));

        doAddLikeRequest(
                notExistedId,
                notExistedId,
                status().isNotFound()
        );
        doRemoveLikeRequest(
                notExistedId,
                notExistedId,
                status().isNotFound()
        );
    }

    @Override
    protected String getRootPath() {
        return "/films";
    }

    @Override
    protected FilmFactory getModelFactory() {
        return this.filmFactory;
    }

    @Override
    protected Film attributesToModel(LinkedHashMap<String, Object> attributes) throws ParseException {
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

    private Film refreshFilm(Film film) {
        return this.filmStorage.findOne(
                film.getId()
        );
    }

    private void doAddLikeRequest(
            Long filmId,
            Long userId
    ) throws Exception {
        doAddLikeRequest(
                filmId,
                userId,
                status().isOk()
        );
    }

    private void doAddLikeRequest(
            Long filmId,
            Long userId,
            ResultMatcher matcher
    ) throws Exception {
        mvc.perform(
                        put(
                                String.format(
                                        "%s/%d/like/%d",
                                        getRootPath(),
                                        filmId,
                                        userId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(matcher);
    }

    private void doRemoveLikeRequest(
            Long filmId,
            Long userId
    ) throws Exception {
        doRemoveLikeRequest(
                filmId,
                userId,
                status().isOk()
        );
    }

    private void doRemoveLikeRequest(
            Long filmId,
            Long userId,
            ResultMatcher matcher
    ) throws Exception {
        mvc.perform(
                        delete(
                                String.format(
                                        "%s/%d/like/%d",
                                        getRootPath(),
                                        filmId,
                                        userId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(matcher);
    }

    private List<Film> doGetPopularRequest() throws Exception {
        return performFilmsRequest(
                makeGetPopularRequestBuilder()
        );
    }

    private List<Film> doGetPopularRequest(int count) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = makeGetPopularRequestBuilder();

        if (count != 0) {
            requestBuilder.queryParam(
                    "count",
                    String.valueOf(count)
            );
        }

        return performFilmsRequest(requestBuilder);
    }

    private MockHttpServletRequestBuilder makeGetPopularRequestBuilder() {
        return get(
                String.format(
                        "%s/popular",
                        getRootPath()
                )
        );
    }

    private List<Film> performFilmsRequest(
            MockHttpServletRequestBuilder requestBuilder
    ) throws Exception {
        return jsonToModels(
                mvc.perform(
                                requestBuilder
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        );
    }

    private void assertIsTopRated(List<Film> films, int expectedSize) {
        List<Integer> rates = films
                .stream()
                .map(film -> refreshFilm(film).getLikes().size())
                .toList();

        Assertions.assertEquals(
                expectedSize,
                rates.size()
        );
        Assertions.assertEquals(
                rates,
                rates
                        .stream()
                        .sorted()
                        .toList()
                        .reversed()
        );
    }

    protected void assertSavedFilm(
            Map<String, Object> expectedData,
            Map<String, Object> savedData
    ) {
        ((HashMap<String, Object>) savedData.getOrDefault("mpa", new HashMap<>()))
                .remove("name");
        ((ArrayList<HashMap<String, Object>>) savedData.getOrDefault("genres", new ArrayList<>()))
                .forEach(
                        (genre) -> genre.remove("name")
                );
        assertSavedData(
                expectedData,
                savedData,
                List.of("releaseDate")
        );
    }
}
