package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractModel {
    private String email;
    private String login;
    private String name;
    private String birthday;
}
