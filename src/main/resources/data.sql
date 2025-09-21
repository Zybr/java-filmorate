-- Fill tables

INSERT INTO genres(name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO mpas(name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO films (name, description, release_date, duration)
VALUES ('The Shawshank Redemption',
        'Two imprisoned men bond over decades, finding solace and eventual redemption.',
        '1994-09-23', 142),
       ('Inception',
        'A skilled thief enters people’s dreams to steal secrets, but faces a dangerous mission of planting an idea instead.',
        '2010-07-16', 148),
       ('The Godfather',
        'The aging patriarch of a powerful crime family hands control to his reluctant son.',
        '1972-03-24', 175),
       ('The Dark Knight',
        'Batman faces his greatest psychological and physical test when the Joker unleashes chaos on Gotham.',
        '2008-07-18', 152),
       ('Forrest Gump',
        'The extraordinary life journey of a simple man who unwittingly influences historic events.',
        '1994-07-06', 142),
       ('The Matrix',
        'A hacker discovers that reality is a simulation and joins a rebellion against the machines.',
        '1999-03-31', 136),
       ('Gladiator',
        'A betrayed Roman general seeks vengeance against the corrupt emperor who murdered his family.',
        '2000-05-05', 155),
       ('Titanic',
        'A romance blossoms aboard the doomed RMS Titanic between a poor artist and a wealthy woman.',
        '1997-12-19', 195),
       ('The Silence of the Lambs',
        'A young FBI trainee seeks the help of an imprisoned cannibal to catch a serial killer.',
        '1991-02-14', 118),
       ('The Lord of the Rings: The Fellowship of the Ring',
        'A hobbit embarks on a quest to destroy a powerful ring that could doom Middle-earth.',
        '2001-12-19', 178);

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 2),
       (2, 6),
       (2, 4),
       (3, 2),
       (4, 6),
       (4, 4),
       (5, 2),
       (5, 1),
       (6, 6),
       (6, 4),
       (7, 6),
       (7, 2),
       (8, 2),
       (8, 1),
       (9, 4),
       (9, 2),
       (10, 2),
       (10, 6);

INSERT INTO film_mpas (film_id, mpa_id)
VALUES (1, 4),
       (2, 3),
       (3, 4),
       (4, 3),
       (5, 3),
       (6, 3),
       (7, 4),
       (8, 3),
       (9, 4),
       (10, 3);

INSERT INTO users (email, login, name, birthday)
VALUES ('alice@example.com', 'alice01', 'Alice', '1990-05-12'),
       ('bob@example.com', 'bobby', 'Bob', '1988-07-23'),
       ('charlie@example.com', 'charlie_c', 'Charlie', '1995-01-03'),
       ('diana@example.com', 'diana88', 'Diana', '1992-09-10'),
       ('edward@example.com', 'eddie', 'Edward', '1985-11-30'),
       ('fiona@example.com', 'fiona92', 'Fiona', '1992-06-15'),
       ('george@example.com', 'geo', 'George', '1989-02-20');


INSERT INTO likes (user_id, film_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 4),
       (3, 5),
       (3, 8),
       (4, 3),
       (4, 9),
       (5, 6),
       (5, 7),
       (6, 10),
       (7, 1),
       (7, 4),
       (7, 6);

INSERT INTO friendships (user_id, friend_id, is_confirmed)
VALUES (1, 2, TRUE),
       (1, 3, TRUE),
       (2, 4, FALSE),
       (3, 5, TRUE),
       (4, 6, TRUE),
       (5, 7, FALSE),
       (6, 7, TRUE);