---- Drop tables

DROP TABLE IF EXISTS friendships;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS mpas;
DROP TABLE IF EXISTS users;

-- Create tables

CREATE TABLE genres
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT genres_name_min_length CHECK (LENGTH(name) > 0)
);

CREATE TABLE mpas
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT mpas_name_min_length CHECK (LENGTH(name) > 0)
);

CREATE TABLE films
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    name         VARCHAR(50)  NOT NULL,
    description  VARCHAR(200) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INTEGER      NOT NULL,
    mpa_id       INTEGER      REFERENCES mpas (id),
    CONSTRAINT films_name_min_length CHECK (LENGTH(name) > 0),
    CONSTRAINT films_duration_range CHECK (duration > 0 AND duration <= 300)
);

CREATE TABLE categories
(
    id       INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    film_id  INTEGER NOT NULL REFERENCES films (id),
    genre_id INTEGER NOT NULL REFERENCES genres (id),
    UNIQUE (film_id, genre_id)
);

CREATE TABLE users
(
    id       INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    email    VARCHAR(50) NOT NULL UNIQUE,
    login    VARCHAR(50) NOT NULL UNIQUE,
    name     VARCHAR(50) UNIQUE,
    birthday DATE        NOT NULL,
    CONSTRAINT users_email_min_length CHECK (LENGTH(email) > 0),
    CONSTRAINT users_login_min_length CHECK (LENGTH(login) > 0),
    CONSTRAINT users_name_min_length CHECK (LENGTH(name) > 0)
);

CREATE TABLE likes
(
    id      INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id INTEGER NOT NULL REFERENCES users (id),
    film_id INTEGER NOT NULL REFERENCES films (id),
    UNIQUE (user_id, film_id)
);

CREATE TABLE friendships
(
    id        INTEGER GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id   INTEGER NOT NULL REFERENCES users (id),
    friend_id INTEGER NOT NULL REFERENCES users (id),
    UNIQUE (user_id, friend_id)
);