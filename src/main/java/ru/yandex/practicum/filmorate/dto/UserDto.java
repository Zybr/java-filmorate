package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String login;
    private String name;
    private Date birthday;
    private final Set<Long> friends = new HashSet<>();
}
