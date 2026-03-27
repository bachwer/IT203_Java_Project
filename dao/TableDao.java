package dao;

import model.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TableDao {
    int create(Table table) throws SQLException;

    boolean update(Table table) throws SQLException;

    boolean delete(int tableId) throws SQLException;

    Optional<Table> findById(int tableId) throws SQLException;

    List<Table> findAll() throws SQLException;

    List<Table> findAvailable() throws SQLException;

    boolean updateStatus(int tableId, String status) throws SQLException;
}
