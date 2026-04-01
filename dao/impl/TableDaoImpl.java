package dao.impl;

import config.DBConnection;
import constance.TableStatus;
import dao.TableDao;
import model.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableDaoImpl implements TableDao {

    @Override
    public int create(Table table) throws SQLException {
        String sql = "INSERT INTO tableRs(name, capacity, status) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, table.getName());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public boolean update(Table table) throws SQLException {
        String sql = "UPDATE tableRs SET name = ?, capacity = ?, status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, table.getName());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus().name());
            ps.setInt(4, table.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int tableId) throws SQLException {
        String sql = "UPDATE tableRs SET status = 'DELETED' WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, tableId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Table> findById(int tableId) throws SQLException {
        String sql = "SELECT * FROM tableRs WHERE id = ? AND status != 'DELETED'";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, tableId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Table table = mapResultSet(rs);
                    return Optional.of(table);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Table> findAll() throws SQLException {
        String sql = "SELECT * FROM tableRs WHERE status != 'DELETED'";
        List<Table> list = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }
        return list;
    }

    @Override
    public List<Table> findAvailable() throws SQLException {
        String sql = "SELECT * FROM tableRs WHERE status = 'AVAILABLE'";
        List<Table> list = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int tableId, String status) throws SQLException {
        String sql = "UPDATE tableRs SET status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt(2, tableId);

            return ps.executeUpdate() > 0;
        }
    }

    // 🔥 Helper method (quan trọng)
    private Table mapResultSet(ResultSet rs) throws SQLException {
        Table table = new Table();
        table.setId(rs.getInt("id"));
        table.setName(rs.getString("name"));
        table.setCapacity(rs.getInt("capacity"));
        table.setStatus(Enum.valueOf(TableStatus.class, rs.getString("status")));
        return table;
    }
}