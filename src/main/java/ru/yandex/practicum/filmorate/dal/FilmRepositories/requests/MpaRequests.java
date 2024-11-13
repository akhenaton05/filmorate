package ru.yandex.practicum.filmorate.dal.FilmRepositories.requests;

public enum MpaRequests {
    FIND_ALL_QUERY("SELECT * FROM rating"),
    FIND_BY_ID_QUERY("SELECT * FROM rating WHERE id = ?");

    public final String query;

    private MpaRequests(String query) {
        this.query = query;
    }
}
