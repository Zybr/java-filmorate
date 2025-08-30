package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.yandex.practicum.filmorate.factory.FilmFactory;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest extends ModelControllerTest<Film> {
    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private FilmService filmService;

    private final FilmFactory filmFactory = new FilmFactory();

    @Autowired
    private UserStorage userStorage;

    private final UserFactory userFactory = new UserFactory();

    /**
     * @see FilmController#addLike(Long, Long)
     */
    @Test
    public void shouldAddLike() throws Exception {
        Film film = createFilm();
        User user = createUser();

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
        Film film = createFilm();
        User user = createUser();
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
        for (int i = 0; i < 12; i++) {
            userStorage.create(userFactory.make());
            filmStorage.create(filmFactory.make());
        }

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
        return new FilmFactory();
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

    private Film createFilm() {
        return filmStorage.create(
                this.filmFactory.make()
        );
    }

    private Film refreshFilm(Film film) {
        return this.filmStorage.findOne(
                film.getId()
        );
    }

    private User createUser() {
        return userStorage.create(
                this.userFactory.make()
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
}
