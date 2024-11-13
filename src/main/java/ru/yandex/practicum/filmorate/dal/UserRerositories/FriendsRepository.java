package ru.yandex.practicum.filmorate.dal.UserRerositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendsRepository extends BaseRepository<Long> {
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO users_friends (user_id, friend_id, status) " +
            "VALUES (?, ?, ?)";
    private static final String FIND_ID_QUERY = "SELECT id FROM users_friends WHERE user_id = ? AND friend_id = ? LIMIT 1";
    private static final String UPDATE_FRIEND_STATUS_QUERY = "UPDATE users_friends " +
            "SET status = ? WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_FRIEND_BY_ID_QUERY2 = "SELECT u.id " +
            "FROM users_friends f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_MUTUAL_FRIEND_QUERY = "SELECT u.id FROM users_friends f JOIN users u ON f.friend_id = u.id " +
            " WHERE f.user_id = ? INTERSECT SELECT u.id FROM users_friends f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?";

    public FriendsRepository(JdbcTemplate jdbc, RowMapper<Long> mapper) {
        super(jdbc, mapper);
    }

    public void insertFriend(long id, long friendId, String status) {
        insert(
                INSERT_FRIEND_QUERY,
                id,
                friendId,
                status
        );
    }

    public Optional<Long> checkFriendship(long id, long friendId) {
        return findOne(FIND_ID_QUERY, id, friendId);
    }

    public void updateFriendsStatus(long id, long friendId, String status) {
        update(
                UPDATE_FRIEND_STATUS_QUERY,
                status,
                id,
                friendId
        );
    }

    public List<Long> getFriendsById(long id) {
        return findMany(
                FIND_ALL_FRIEND_BY_ID_QUERY2,
                id
        );
    }

    public void deleteFriend(long id, long friendId) {
        update(
                DELETE_FRIEND_QUERY,
                id,
                friendId
        );
    }

    public List<Long> getMutualFriends(long id, long friendId) {
        return findMany(
                FIND_MUTUAL_FRIEND_QUERY,
                id,
                friendId
        );
    }
}
