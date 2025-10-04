package ru.yandex.practicum.filmorate.model;


import lombok.Data;

@Data
public class Model {
    protected Long id;

    public static Model getInstance(Long id) {
        Model model = new Model();
        model.setId(id);
        return model;
    }
}
