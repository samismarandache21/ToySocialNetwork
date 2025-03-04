package repository.database;

import domain.Mesaj;
import domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.sql.DriverManager.getConnection;

public class MesajeDatabaseRepository extends AbstractDatabaseRepository<Long, Mesaj> {

    public MesajeDatabaseRepository(Validator<Mesaj> validator, String tabelDat) {
        super(validator, tabelDat);
    }

    @Override
    public Mesaj insertQuery(Mesaj mesaj, Connection conn) {
        // Step 1: Find or create the conversation
        Long conversationId = findConversation(mesaj.getFrom(), mesaj.getTo().get(0))
                .orElseGet(() -> createConversation(mesaj.getFrom(), mesaj.getTo().get(0)));

        // Step 2: Insert the message
        String sqlStat = "INSERT INTO " + super.getTabel() + " (mesaj, from_user_id, data, conversation_id, reply_to) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlStat, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, mesaj.getMesaj());
            ps.setLong(2, mesaj.getFrom());
            ps.setTimestamp(3, Timestamp.valueOf(mesaj.getData())); // Convert LocalDateTime to Timestamp
            ps.setLong(4, conversationId); // Use the conversation ID

            if (mesaj.getReplyTo() != null) {
                ps.setLong(5, mesaj.getReplyTo());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                mesaj.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Inserting message failed, no ID obtained.");
            }
            return mesaj;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int deleteQuery(Long aLong, Connection conn) {
        String sqlStat = "DELETE FROM " + super.getTabel() + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlStat)) {
            ps.setLong(1, aLong);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateQuery(Mesaj mesaj, Connection conn) {
        String sqlStat = "UPDATE " + super.getTabel() + " SET mesaj = ?, from_user_id = ?, data = ?, conversation_id = ?, reply_to = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlStat)) {
            ps.setString(1, mesaj.getMesaj());
            ps.setLong(2, mesaj.getFrom());
            ps.setTimestamp(3, Timestamp.valueOf(mesaj.getData())); // Convert LocalDateTime to Timestamp
            ps.setLong(4, mesaj.getTo().get(0)); // Assuming one recipient for now
            if (mesaj.getReplyTo() != null) {
                ps.setLong(5, mesaj.getReplyTo());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setLong(6, mesaj.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mesaj createEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String mesajText = rs.getString("mesaj");
        Long fromUserId = rs.getLong("from_user_id");
        LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
        Long conversationId = rs.getLong("conversation_id");
        Long replyTo = rs.getObject("reply_to", Long.class);

        List<Long> recipients = new ArrayList<>();
        recipients.add(conversationId);

        Mesaj res = new Mesaj(mesajText, fromUserId, recipients, data, replyTo);
        res.setId(id);
        return res;
    }

    public List<Mesaj> getMessagesByConversation(Long conversationId) {
        List<Mesaj> messages = new ArrayList<>();
        String sqlStat = "SELECT * FROM " + super.getTabel() + " WHERE conversation_id = ?";
        try (Connection conn = getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sqlStat)) {
            ps.setLong(1, conversationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(createEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

    public Optional<Long> findConversation(Long userId1, Long userId2) {
        String query = "SELECT id FROM conversations WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
        try (Connection conn = getConnection(url,user,password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.setLong(3, userId2);
            stmt.setLong(4, userId1);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public Long createConversation(Long userId1, Long userId2) {
        String query = "INSERT INTO conversations (user1_id, user2_id) VALUES (?, ?) RETURNING id";
        try (Connection conn = getConnection(url,user,password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to create a new conversation!");
    }




}
