package ru.yandex.practicum.filmorate.dal.UserRerositories.requests;

public enum FriendsRequests {
    INSERT_FRIEND_QUERY("INSERT INTO users_friends (user_id, friend_id, status) " +
            "VALUES (?, ?, ?)"),
    FIND_ID_QUERY("SELECT id FROM users_friends WHERE user_id = ? AND friend_id = ? LIMIT 1"),
    UPDATE_FRIEND_STATUS_QUERY("UPDATE users_friends " +
            "SET status = ? WHERE user_id = ? AND friend_id = ?"),
    FIND_ALL_FRIEND_BY_ID_QUERY2("SELECT u.id " +
            "FROM users_friends f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?"),
    DELETE_FRIEND_QUERY("DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?"),
    FIND_MUTUAL_FRIEND_QUERY("SELECT u.id FROM users_friends f JOIN users u ON f.friend_id = u.id " +
            " WHERE f.user_id = ? INTERSECT SELECT u.id FROM users_friends f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?");

    public final String query;

    private FriendsRequests(String query) {
        this.query = query;
    }
}
