package ru.yandex.practicum.filmorate.dal.FilmRepositories.requests;

public enum FilmRequests {
    FIND_ALL_QUERY("SELECT * FROM films"),
    FIND_BY_ID_QUERY("SELECT * FROM films WHERE id = ?"),
    INSERT_QUERY("INSERT INTO films (name, description, release_date, duration, likes_count, rating_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)"),
    UPDATE_QUERY("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, likes_count = ?, rating_id = ? " +
            "WHERE id = ?"),
    DELETE_BY_ID_QUERY("DELETE FROM films WHERE id = ?"),
    POPULAR_FILM_QUERY("SELECT * FROM films f JOIN (SELECT film_id, COUNT(user_id) AS like_count" +
            " FROM users_likes GROUP BY film_id ORDER BY like_count DESC) l ON f.id=l.film_id LIMIT ?");

    public final String query;

    private FilmRequests(String query) {
        this.query = query;
    }
}
