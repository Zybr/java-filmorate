-- Selections

-- Straightforward selections
SELECT *
FROM genres;
SELECT *
FROM mpas;
SELECT *
FROM films;
SELECT *
FROM film_genres;
SELECT *
FROM film_mpas;
SELECT *
FROM users;
SELECT *
FROM likes;
SELECT *
FROM friendships;

-- Films with genres
SELECT f.id, f.name AS film, STRING_AGG(g.name, ',') AS genre
FROM films f
         JOIN film_genres fg ON f.id = fg.film_id
         JOIN genres g ON fg.genre_id = g.id
GROUP BY f.id, f.name
ORDER BY f.name;

-- Films with ratings
SELECT f.name AS film, m.name AS mpa_rating
FROM films f
         JOIN film_mpas fm ON f.id = fm.film_id
         JOIN mpas m ON fm.mpa_id = m.id;

-- Films with likes
SELECT f.name AS film, count(l.user_id), string_agg(u.name, ',') AS like_count
FROM films f
         LEFT JOIN likes l ON f.id = l.film_id
         LEFT JOIN users u ON u.id = l.user_id
GROUP BY f.id, f.name
ORDER BY like_count desc;

-- User friends
SELECT u.name AS user_name, f.name AS friend_name, fr.is_confirmed
FROM friendships fr
         JOIN users u ON fr.user_id = u.id
         JOIN users f ON fr.friend_id = f.id
WHERE u.id = 1;

-- The top genre
SELECT g.name AS genre, count(l.user_id) AS total_likes
FROM likes l
         JOIN films f ON l.film_id = f.id
         JOIN film_genres fg ON f.id = fg.film_id
         JOIN genres g ON fg.genre_id = g.id
GROUP BY g.name
ORDER BY total_likes desc
LIMIT 1;

-- The longest film
SELECT name, duration
FROM films
WHERE duration = (SELECT max(duration) FROM films);