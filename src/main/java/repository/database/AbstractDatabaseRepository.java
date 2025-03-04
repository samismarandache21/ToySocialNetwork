package repository.database;

import domain.Entity;
import domain.validators.Validator;
import repository.Repository;
import repository.memory.InMemoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.sql.*;

public abstract class AbstractDatabaseRepository <ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String url = "jdbc:postgresql://localhost:5432/reteasociala";
    protected String user = "postgres";
    protected String password = "postgres";
    protected String tabel;
    protected Connection conn;


    public AbstractDatabaseRepository(Validator<E> validator, String tabelDat) {
        super(validator);
        tabel = tabelDat;
        loadData();
    }

    private void loadData() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            entities = new HashMap<>();
            String getAll = "SELECT * FROM " + tabel;
            PreparedStatement ps = conn.prepareStatement(getAll);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                E newEntity = createEntity(rs);
                super.save(newEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            E newUtilizator = insertQuery(entity, conn);  // Aici presupun că este implementată logica de inserare a mesajului.
            Optional<E> saveResult = super.save(newUtilizator);  // Probabil aici ar trebui să salvați în repository-ul propriu.
            loadData();  // Actualizează datele?
            return saveResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // Aruncă o excepție dacă ceva nu merge bine
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Optional<E> saveResult = super.delete(id);
            if (saveResult.isEmpty()) {
                return saveResult;
            }
            deleteQuery(id,conn);
            return saveResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> update(E entity) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Optional<E> saveResult = super.update(entity);
            if (saveResult.isEmpty()) {
                return saveResult;
            }
            updateQuery(entity,conn);
            return saveResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public Map<ID, E> getEntities() {
        loadData();
        return entities;
    }

    public String getTabel(){
        return tabel;
    }

    protected abstract E insertQuery(E enitity, Connection conn);
    protected abstract int deleteQuery(ID id, Connection conn);
    protected abstract int updateQuery(E enitity, Connection conn);
    protected abstract E createEntity(ResultSet rs) throws SQLException;
}