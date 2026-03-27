package dao.impl;

import config.DBConnection;
import dao.ReviewDao;
import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewImpl implements ReviewDao {
    @Override
    public int create(Review review) throws SQLException {
        String sql = "INSERT INTO review(userId, rating, comment) value(?,?,?)";
        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    @Override
    public List<Review> findAll() throws SQLException {
        String sql = "SELECT id, userId, rating, comment, createdAt from review  ORDER BY createdAt DESC";

        List<Review> review = new ArrayList<>();
        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ){
            while(rs.next()){
                Timestamp created = rs.getTimestamp("createdAt");
                review.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        created == null ? null : created.toLocalDateTime()
                ));
            }


        }
        return review;
    }
}
