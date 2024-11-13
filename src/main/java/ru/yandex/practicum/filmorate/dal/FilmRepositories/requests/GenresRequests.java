package ru.yandex.practicum.filmorate.dal.FilmRepositories.requests;

public enum GenresRequests {
    FIND_ALL_QUERY("SELECT * FROM genres"),
    FIND_BY_ID_QUERY("SELECT * FROM genres WHERE genre_id = ?");

    public final String query;

    private GenresRequests(String query) {
        this.query = query;
    }
}
