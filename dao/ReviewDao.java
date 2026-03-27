package dao;

import model.Review;

import java.sql.SQLException;
import java.util.List;

public interface ReviewDao {
    int create(Review review) throws SQLException;

    List<Review> findAll() throws SQLException;
}
