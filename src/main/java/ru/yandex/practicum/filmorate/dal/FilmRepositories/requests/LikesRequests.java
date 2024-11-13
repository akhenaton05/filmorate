package ru.yandex.practicum.filmorate.dal.FilmRepositories.requests;

public enum LikesRequests {
    INSERT_LIKE_QUERY("INSERT INTO users_likes (film_id, user_id) VALUES (?, ?)"),
    DELETE_LIKE_QUERY("DELETE FROM users_likes WHERE film_id = ? AND user_id = ?");

    public final String query;

    private LikesRequests(String query) {
        this.query = query;
    }
}
