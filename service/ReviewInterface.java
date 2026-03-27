package service;

import model.Review;

import java.util.List;


public interface ReviewInterface {
    void addReview(int userId, int rating, String comment);

    List<Review> getAllReviews();
}
