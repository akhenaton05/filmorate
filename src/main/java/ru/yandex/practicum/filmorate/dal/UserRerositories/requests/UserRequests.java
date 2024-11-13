package ru.yandex.practicum.filmorate.dal.UserRerositories.requests;

public enum UserRequests {
    FIND_ALL_QUERY("SELECT * FROM users"),
    FIND_BY_ID_QUERY("SELECT * FROM users WHERE id = ?"),
    FIND_BY_EMAIL_QUERY("SELECT * FROM users WHERE email = ?"),
    INSERT_QUERY("INSERT INTO users (email, login, name, birthday_date)" +
            "VALUES (?, ?, ?, ?)"),
    UPDATE_QUERY("UPDATE users SET email = ?, login = ?, name = ?, birthday_date = ? " +
            "WHERE id = ?"),
    DELETE_BY_ID_QUERY("DELETE FROM users WHERE id = ?");

    public final String query;

    private UserRequests(String query) {
        this.query = query;
    }
}
