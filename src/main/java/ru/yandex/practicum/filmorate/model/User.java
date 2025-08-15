package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class User extends AbstractModel {
    protected Long id;
    private String email;
    private String login;
    private String name;
    private String birthday;
}
