package repository.database;

import com.example.reteasociala.paging.Page;
import com.example.reteasociala.paging.Pageable;
import domain.Friendship;
import domain.Tuple;
import domain.Utilizator;
import domain.validators.Validator;
import repository.FriendshipRepository;
import repository.PagedRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<Long,Long>,Friendship> implements FriendshipRepository {
    public FriendshipDatabaseRepository(Validator<Friendship> validator, String tabelDat) {
        super(validator, tabelDat);
    }
    @Override
    public Friendship insertQuery(Friendship entity, Connection conn) {
        String sqlStat =  "INSERT INTO " + super.getTabel() + "(userId1,userId2,dateTime,status) VALUES (?,?,?,?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat,Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,entity.getId().getFirst());
            ps.setLong(2,entity.getId().getSecond());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getDataPrietenie()));
            ps.setInt(4,entity.getStatus());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(new Tuple<Long,Long>(generatedKeys.getLong(1),generatedKeys.getLong(2)));
            } else {
                throw new SQLException("Sending message failed, no ID obtained.");
            }
            return entity;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteQuery(Tuple<Long, Long> longLongTuple,Connection conn) {
        String sqlStat = "DELETE FROM " + super.getTabel() + " WHERE (userId1 = ? AND userId2 = ?) OR (userId2 = ? AND userId1 = ?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,longLongTuple.getFirst());
            ps.setLong(2,longLongTuple.getSecond());
            ps.setLong(3,longLongTuple.getFirst());
            ps.setLong(4,longLongTuple.getSecond());

            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateQuery(Friendship entity,Connection conn) {
        String sqlStat = "UPDATE  " + super.getTabel() + " SET userId1 = ?, userId2 = ?, dateTime = ?, status = ?  WHERE (userId1 = ? AND userId2 = ?) OR (userId2 = ? AND userId1 = ?)";
        try(PreparedStatement ps = conn.prepareStatement(sqlStat)){
            ps.setLong(1,entity.getId().getFirst());
            ps.setLong(2,entity.getId().getSecond());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getDataPrietenie()));
            ps.setInt(4,entity.getStatus());
            ps.setLong(5,entity.getId().getFirst());
            ps.setLong(6,entity.getId().getSecond());
            ps.setLong(7,entity.getId().getFirst());
            ps.setLong(8,entity.getId().getSecond());

            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Friendship createEntity(ResultSet rs) throws SQLException {
        Friendship f  = new Friendship(rs.getTimestamp("dateTime").toLocalDateTime(),rs.getInt("status"));
        f.setId(new Tuple<>(rs.getLong("userId1"), rs.getLong("userId2")));
        return f;
    }

    private int countUserFriendships(Connection connection, Long userId) throws SQLException {
        try {
            String sql = "select count(*) as count from prietenii where (userid1 = ? OR userid2 = ?) AND status = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1,userId);
            statement.setLong(2,userId);
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfFriends = 0;
                if (result.next()) {
                    totalNumberOfFriends = result.getInt("count");
                }
                return totalNumberOfFriends;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Friendship> getPagedUserFriendships(Connection connection, Pageable pageable, Long userId) {
        List<Friendship> friendshipsOnPage = new ArrayList<>();
        try {
            String sql = "select * from prietenii where (userid1 = ? OR userid2 = ?) AND status = 1 limit ? offset ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1,userId);
            statement.setLong(2,userId);
            statement.setInt(3,pageable.getPageSize());
            statement.setInt(4,pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long userid1 = resultSet.getLong("userid1");
                    Long userid2 = resultSet.getLong("userid2");
                    Date datetime = resultSet.getDate("datetime");
                    Integer status = resultSet.getInt("status");
                    Friendship friendship = new Friendship(datetime.toLocalDate().atTime(LocalDateTime.now().toLocalTime()), status);
                    friendship.setId(new Tuple<Long, Long>(userid1, userid2));
                    friendshipsOnPage.add(friendship);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendshipsOnPage;
    }

    @Override
    public Page<Friendship> findAllUserFriendshipsOnPage(Pageable pageable, Long userId) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            int totalNumberOfUserFriendships = countUserFriendships(connection, userId);
            List<Friendship> friendshipsOnPage;
            if (totalNumberOfUserFriendships > 0) {
                friendshipsOnPage = getPagedUserFriendships(connection, pageable, userId);
            } else {
                friendshipsOnPage = new ArrayList<>();
            }
            return new Page<>(friendshipsOnPage, totalNumberOfUserFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable) {
        return findAllUserFriendshipsOnPage(pageable,null);
    }
}
