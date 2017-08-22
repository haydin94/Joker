/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  aydins
 * Created: 11.08.2017
 */

-- VIEW_ALLJOKES
CREATE OR REPLACE VIEW view_allJokes
AS SELECT 
j.joke_id AS j_id, j.user_id AS j_userId, j.category AS j_category, j.date AS j_date, j.joke AS j_joke, j.rating AS j_rating, j.numRating AS j_numRating, j.numComments AS j_numComm, j.active AS j_active,
uj.user_id AS uj_id, uj.name AS uj_name, uj.age AS uj_age, uj.gender AS uj_gender, uj.date AS uj_date, uj.description AS uj_desc, uj.place AS uj_place, uj.favourite AS uj_fav, uj.active AS uj_active, 
c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active, 
uc.user_id AS uc_id, uc.name AS uc_name, uc.age AS uc_age, uc.gender AS uc_gender, uc.date AS uc_date, uc.description AS uc_desc, uc.place AS uc_place, uc.favourite AS uc_fav, uc.active AS uc_active
FROM tab_comment c 
LEFT JOIN tab_comment c2 ON c.joke_id = c2.joke_id AND c.rating < c2.rating
RIGHT OUTER JOIN tab_joke j ON c.joke_id = j.joke_id
LEFT OUTER JOIN tab_user uc ON c.user_id = uc.user_id
INNER JOIN tab_user uj ON j.user_id = uj.user_id
WHERE c2.rating is NULL
ORDER BY j.joke_id ASC


-- VIEW_ALLJOKES        -- NICHT IN BENUTZUNG!
-- CREATE OR REPLACE VIEW view_allJokes2
-- AS SELECT 
-- u.user_id AS u_id, u.name AS u_name, u.age AS u_age, u.gender AS u_gender, u.date AS u_date, u.description AS u_desc, u.place AS u_place, u.favourite AS u_fav, u.active AS u_active,
-- j.joke_id AS j_id, j.user_id AS j_userId, j.category AS j_category, j.date AS j_date, j.joke AS j_joke, j.rating AS j_rating, j.numRating AS j_numRating, j.numComments AS j_numComm, j.active AS j_active,
-- uj.user_id AS uj_id, uj.name AS uj_name, uj.age AS uj_age, uj.gender AS uj_gender, uj.date AS uj_date, uj.description AS uj_desc, uj.place AS uj_place, uj.favourite AS uj_fav, uj.active AS uj_active, 
-- c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active, 
-- uc.user_id AS uc_id, uc.name AS uc_name, uc.age AS uc_age, uc.gender AS uc_gender, uc.date AS uc_date, uc.description AS uc_desc, uc.place AS uc_place, uc.favourite AS uc_fav, uc.active AS uc_active
-- FROM tab_comment c 
-- LEFT JOIN tab_comment c2 ON c.joke_id = c2.joke_id AND c.rating < c2.rating
-- RIGHT OUTER JOIN tab_joke j ON c.joke_id = j.joke_id
-- LEFT OUTER JOIN tab_user uc ON c.user_id = uc.user_id
-- INNER JOIN tab_user uj ON j.user_id = uj.user_id
-- INNER JOIN tab_user u ON u.user_id = j.user_id
-- WHERE c2.rating is NULL
-- ORDER BY j.joke_id ASC


-- View_JOKEVIEW
CREATE OR REPLACE VIEW view_jokeView
AS SELECT 
j.joke_id AS j_id, j.user_id AS j_userId, j.category AS j_category, j.date AS j_date, j.joke AS j_joke, j.rating AS j_rating, j.numRating AS j_numRating, j.numComments AS j_numComm, j.active AS j_active,
uj.user_id AS uj_id, uj.name AS uj_name, uj.age AS uj_age, uj.gender AS uj_gender, uj.date AS uj_date, uj.description AS uj_desc, uj.place AS uj_place, uj.favourite AS uj_fav, uj.active AS uj_active,
c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active,
uc.user_id AS uc_id, uc.name AS uc_name, uc.age AS uc_age, uc.gender AS uc_gender, uc.date AS uc_date, uc.description AS uc_desc, uc.place AS uc_place, uc.favourite AS uc_fav, uc.active AS uc_active
FROM tab_joke j
INNER JOIN tab_user uj ON j.user_id = uj.user_id
LEFT OUTER JOIN tab_comment c ON j.joke_id = c.joke_id
LEFT OUTER JOIN tab_user uc ON uc.user_id = c.user_id       -- Weil sonst wenn c.id = NULL und Inner Join würde garnicht anzeigen!
ORDER BY j.joke_id ASC


-- View_JOKECARD
CREATE OR REPLACE VIEW view_jokeCard
AS SELECT 
j.joke_id AS j_id, j.user_id AS j_userId, j.category AS j_category, j.date AS j_date, j.joke AS j_joke, j.rating AS j_rating, j.numRating AS j_numRating, j.numComments AS j_numComm, j.active AS j_active,
uj.user_id AS uj_id, uj.name AS uj_name, uj.age AS uj_age, uj.gender AS uj_gender, uj.date AS uj_date, uj.description AS uj_desc, uj.place AS uj_place, uj.favourite AS uj_fav, uj.active AS uj_active
FROM tab_joke j, tab_user uj   
AND j.user_id = uj.user_id


-- VIEW_JOKECOMMETS  -> Einfache View um zu sehen welche Kommentare zu welchem Joke gehören -- Zur eigenen Einsicht
CREATE OR REPLACE VIEW view_jokeComments
AS SELECT 
j.joke_id AS j_id, j.user_id AS j_userId, j.category AS j_category, j.date AS j_date, j.joke AS j_joke, j.rating AS j_rating, j.numRating AS j_numRating, j.numComments AS j_numComm, j.active AS j_active,
c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active
FROM tab_joke j
LEFT OUTER JOIN tab_comment c ON j.joke_id = c.joke_id
ORDER BY j.joke_id ASC




-- VIEW_MYCOMMENTS
CREATE OR REPLACE VIEW view_myComments
AS SELECT 
c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active, 
FROM tab_comment c 


-- VIEW_USERCARD
CREATE OR REPLACE VIEW view_userCard
AS SELECT 
u.user_id AS u_id, u.name AS u_name, u.age AS u_age, u.gender AS u_gender, u.date AS u_date, u.description AS u_desc, u.place AS u_place, u.favourite AS u_fav, u.email AS u_email, u.active AS u_active
FROM tab_user u


-- view_commentCard
CREATE OR REPLACE VIEW view_commentCard
AS SELECT 
c.com_id AS c_id, c.user_id AS c_userId, c.joke_id AS c_jokeId, c.date AS c_date, c.comment AS c_comment, c.rating AS c_rating, c.numRating AS c_numRating, c.active AS c_active,
uc.user_id AS uc_id, uc.name AS uc_name, uc.age AS uc_age, uc.gender AS uc_gender, uc.date AS uc_date, uc.description AS uc_desc, uc.place AS uc_place, uc.favourite AS uc_fav, uc.active AS uc_active
FROM tab_comment c, tab_user uc
WHERE c.user_id = uc.user_id
