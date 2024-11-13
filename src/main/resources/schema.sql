CREATE TABLE IF NOT EXISTS users (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            email VARCHAR(255) NOT NULL,
            login VARCHAR(40) NOT NULL,
            name VARCHAR(40),
            birthday_date DATE
            );

CREATE TABLE IF NOT EXISTS rating (
            id BIGINT PRIMARY KEY,
            name VARCHAR(255)
            );

CREATE TABLE IF NOT EXISTS films (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            name VARCHAR(40),
            description VARCHAR(200),
            release_date DATE,
            duration INT,
            likes_count INT,
            rating_id INT REFERENCES rating(id)
            );

CREATE TABLE IF NOT EXISTS genres (
            genre_id BIGINT PRIMARY KEY,
            name VARCHAR(40)
            );

CREATE TABLE IF NOT EXISTS films_genres (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            film_id BIGINT REFERENCES films(id),
            genre_id BIGINT REFERENCES genres(genre_id)
            );

CREATE TABLE IF NOT EXISTS users_likes (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            user_id BIGINT REFERENCES users(id),
            film_id BIGINT REFERENCES films(id)
            );

CREATE TABLE IF NOT EXISTS users_friends (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            user_id BIGINT REFERENCES users(id),
            friend_id BIGINT REFERENCES users(id),
            status VARCHAR(40) NOT NULL
            );