-- Checks
select 'genres' as "table";
SELECT * FROM genres;
select 'mpas' as "table";
SELECT * FROM mpas;
select 'films' as "table";
SELECT id, name FROM films;
select 'users' as "table";
SELECT * FROM users;
SELECT count(*) as "categories size" FROM categories;
SELECT count(*) as "film_mpas size" FROM film_mpas;
SELECT count(*) as "film_likes size" FROM likes;
SELECT count(*) as "friendships size" FROM friendships;