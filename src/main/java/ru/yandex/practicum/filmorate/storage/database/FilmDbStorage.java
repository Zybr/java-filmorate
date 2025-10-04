package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private final FilmRepository repository;
    private final LikeRepository likeRepository;
    private final MpaRepository mpaRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<Film> findAll() {
        Map<Long, Set<Long>> filmLikes = getFilmLikes();
        Map<Long, Mpa> filmMpas = getFilmMpas();
        Map<Long, List<Genre>> filmGenres = getFilmGenres();

        return super
                .findAll()
                .stream()
                .peek(
                        film -> completeWithRelations(
                                film,
                                filmLikes,
                                filmMpas,
                                filmGenres
                        )
                )
                .toList();
    }

    @Override
    public Film findOrFail(Long id) {
        return completeWithRelations(
                super.findOrFail(id)
        );
    }

    @Override
    public Film findOne(Long id) {
        Film film = super.findOne(id);

        if (film != null) {
            this.completeWithRelations(film);
        }

        return film;
    }

    @Override
    public Film create(Film creation) {
        Film film = super.create(creation);
        saveGenres(film);
        return completeWithRelations(film);
    }

    @Override
    public Film update(Film updating) {
        Film film = super.update(updating);
        saveGenres(film);
        return completeWithRelations(film);
    }

    protected BaseModelRepository<Film> getRepository() {
        return repository;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    private Film completeWithRelations(
            Film film
    ) {
        completeWithLikes(film, getFilmLikes());
        completeWithMpa(film, getFilmMpas());
        completeWithGenres(film, getFilmGenres());
        return film;
    }

    private void completeWithRelations(
            Film film,
            Map<Long, Set<Long>> filmLikes,
            Map<Long, Mpa> filmMpas,
            Map<Long, List<Genre>> filmGenres
    ) {
        completeWithLikes(film, filmLikes);
        completeWithMpa(film, filmMpas);
        completeWithGenres(film, filmGenres);
    }

    private void completeWithLikes(
            Film film,
            Map<Long, Set<Long>> filmLikes
    ) {
        film.getLikes()
                .addAll(
                        filmLikes
                                .getOrDefault(
                                        film.getId(),
                                        new HashSet<>()
                                )
                                .stream()
                                .toList()
                );
    }

    private void completeWithMpa(
            Film film,
            Map<Long, Mpa> filmMpas
    ) {
        film.setMpa(
                filmMpas.getOrDefault(
                        film.getMpaId(),
                        null
                )
        );

    }

    private void completeWithGenres(
            Film film,
            Map<Long, List<Genre>> filmGenres
    ) {
        film.getGenres()
                .addAll(
                        filmGenres
                                .getOrDefault(film.getId(), new ArrayList<>())
                                .stream()
                                .toList()
                );
    }

    private Map<Long, Set<Long>> getFilmLikes() {
        return likeRepository
                .findAll()
                .stream()
                .reduce(
                        new HashMap<>(),
                        (map, like) -> {
                            map.put(
                                    like.getFilmId(),
                                    map.getOrDefault(
                                            like.getFilmId(),
                                            new HashSet<>()
                                    )
                            );
                            map
                                    .get(like.getFilmId())
                                    .add(like.getUserId());
                            return map;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }

    private Map<Long, Mpa> getFilmMpas() {
        return this.mpaRepository
                .findAll()
                .stream()
                .reduce(
                        new HashMap<Long, Mpa>(),
                        (modelsMap, model) -> {
                            modelsMap.put(model.getId(), model);
                            return modelsMap;
                        },
                        (HashMap<Long, Mpa> mpaA, HashMap<Long, Mpa> mpaB) -> {
                            mpaA.putAll(mpaB);
                            return mpaA;
                        }
                );
    }

    private Map<Long, List<Genre>> getFilmGenres() {
        Map<Long, Genre> genres = getGenres();

        return categoryRepository
                .findAll()
                .stream()
                .reduce(
                        new HashMap<>(),
                        (map, category) -> {
                            map.put(
                                    category.getFilmId(),
                                    map.getOrDefault(
                                            category.getFilmId(),
                                            new ArrayList<>()
                                    )
                            );
                            map
                                    .get(category.getFilmId())
                                    .add(
                                            genres.get(
                                                    category.getGenreId()
                                            )
                                    );
                            return map;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }

    private Map<Long, Genre> getGenres() {
        return genreRepository
                .findAll()
                .stream()
                .reduce(
                        new HashMap<>(),
                        (map, genre) -> {
                            map.put(
                                    genre.getId(),
                                    genre
                            );
                            return map;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }

    private void saveGenres(Film film) {
        categoryRepository
                .deleteByFilmId(film.getId());
        film.getGenreIds()
                .forEach(
                        (Long genreId) -> categoryRepository.insert(
                                Category.builder()
                                        .filmId(film.getId())
                                        .genreId(genreId)
                                        .build()
                        )
                );
    }
}
