package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface CategoryStorage {
    List<Category> findAll();

    void addCategory(Film film, Genre genre);

    void removeCategory(Film film, Genre genre);
}
