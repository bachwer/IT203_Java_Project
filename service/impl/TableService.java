package service.impl;

import constance.TableStatus;
import dao.TableDao;
import dao.impl.TableDaoImpl;
import model.Table;
import service.TableInterface;
import util.InputValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TableService implements TableInterface {
    private final TableDao tableDao = new TableDaoImpl();

    @Override
    public int create(Table table) {
        try {
            validate(table);
            if (isDuplicateNameForCreate(table.getName())) {
                throw new IllegalArgumentException("Table name already exists.");
            }
            return tableDao.create(table);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create table: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Table table) {
        try {
            validate(table);
            if (isDuplicateNameForUpdate(table.getId(), table.getName())) {
                throw new IllegalArgumentException("Table name already exists.");
            }
            if (!tableDao.update(table)) {
                throw new IllegalArgumentException("Table not found.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update table: " + e.getMessage(), e);
        }
    }
    @Override
    public void delete(int tableId) {
        try {
            if (!tableDao.delete(tableId)) {
                throw new IllegalArgumentException("Table not found.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot delete table: " + e.getMessage(), e);
        }
    }
    @Override
    public Table findByIdTable(int id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid ID.");
            }
            Optional<Table> item = tableDao.findById(id);
            return item.orElse(null);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot find table: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Table> getAll() {
        try {
            return tableDao.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get tables: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Table> getAvailable() {
        try {
            return tableDao.findAvailable();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get available tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void markAvailable(int tableId) {
        try {
            tableDao.updateStatus(tableId, TableStatus.AVAILABLE.name());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot update table status: " + e.getMessage(), e);
        }
    }
    @Override
    public void markOccupied(int tableId) {
        try {
            tableDao.updateStatus(tableId, TableStatus.OCCUPIED.name());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot update table status: " + e.getMessage(), e);
        }
    }

    @Override
    public void validate(Table table) {
        if (table == null || InputValidator.isNotBlank(table.getName())) {
            throw new IllegalArgumentException("Table name is required.");
        }
        if (InputValidator.isPositiveInt(table.getCapacity())) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        if (table.getStatus() == null) {
            table.setStatus(TableStatus.AVAILABLE);
        }
    }

    private boolean isDuplicateNameForCreate(String name) throws SQLException {
        String normalized = normalizeName(name);
        return tableDao.findAll().stream()
                .anyMatch(table -> normalizeName(table.getName()).equalsIgnoreCase(normalized));
    }

    private boolean isDuplicateNameForUpdate(int id, String name) throws SQLException {
        String normalized = normalizeName(name);
        return tableDao.findAll().stream()
                .anyMatch(table -> table.getId() != id && normalizeName(table.getName()).equalsIgnoreCase(normalized));
    }

    private String normalizeName(String value) {
        return value == null ? "" : value.trim();
    }


}
