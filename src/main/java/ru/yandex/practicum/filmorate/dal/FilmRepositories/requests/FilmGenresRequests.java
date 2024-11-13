package ru.yandex.practicum.filmorate.dal.FilmRepositories.requests;

public enum FilmGenresRequests {
    DELETE_QUERY("DELETE FROM films_genres WHERE film_id = ?"),
    FIND_FILMS_GENRES_QUERY("SELECT g.genre_id, g.name FROM films_genres f JOIN genres g ON f.genre_id=g.genre_id WHERE f.film_id = ?");

    public final String query;

    private FilmGenresRequests(String query) {
        this.query = query;
    }
}
