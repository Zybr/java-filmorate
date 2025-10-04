package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaStorage mpaRepository;

    @GetMapping("/{id}")
    public Mpa getOne(
            @PathVariable Long id
    ) {
        return mpaRepository.findOrFail(id);
    }

    @GetMapping
    public List<Mpa> getAll() {
        return mpaRepository.findAll();
    }

}
