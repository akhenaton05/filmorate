package ru.yandex.practicum.filmorate.dal.UserRerositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.UserRerositories.requests.FriendsRequests.*;

@Repository
public class FriendsRepository extends BaseRepository<Long> {
    public FriendsRepository(JdbcTemplate jdbc, RowMapper<Long> mapper) {
        super(jdbc, mapper);
    }

    public void insertFriend(long id, long friendId, String status) {
        insert(
                INSERT_FRIEND_QUERY.query,
                id,
                friendId,
                status
        );
    }

    public Optional<Long> checkFriendship(long id, long friendId) {
        return findOne(FIND_ID_QUERY.query, id, friendId);
    }

    public void updateFriendsStatus(long id, long friendId, String status) {
        update(
                UPDATE_FRIEND_STATUS_QUERY.query,
                status,
                id,
                friendId
        );
    }

    public List<Long> getFriendsById(long id) {
        return findMany(
                FIND_ALL_FRIEND_BY_ID_QUERY2.query,
                id
        );
    }

    public void deleteFriend(long id, long friendId) {
        update(
                DELETE_FRIEND_QUERY.query,
                id,
                friendId
        );
    }

    public List<Long> getMutualFriends(long id, long friendId) {
        return findMany(
                FIND_MUTUAL_FRIEND_QUERY.query,
                id,
                friendId
        );
    }
}
