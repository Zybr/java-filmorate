package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.CategoryRepository;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.CategoryStorage;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoryDbStorage implements CategoryStorage {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Film film, Genre genre) {
        if (hasCategory(film, genre)) {
            return;
        }

        categoryRepository.insertOne(
                Category.builder()
                        .filmId(film.getId())
                        .genreId(genre.getId())
                        .build()
        );

        film.getGenres()
                .add(genre);
        log.info(
                "Genre(ID:{}) was added to Film({}).",
                genre.getId(),
                film.getId()
        );
    }

    @Override
    public void removeCategory(Film film, Genre genre) {
        if (!hasCategory(film, genre)) {
            return;
        }

        categoryRepository.deleteByFilmGenreIds(
                film.getId(),
                genre.getId()
        );
        film.getGenres()
                .remove(genre.getId());
        log.info(
                "Genre(ID:{}) was removed from Film({}).",
                genre.getId(),
                film.getId()
        );
    }

    private boolean hasCategory(Film film, Genre genre) {
        return film
                .getGenres()
                .contains(genre.getId());
    }
}
