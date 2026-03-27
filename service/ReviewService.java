package service;

import dao.ReviewDao;
import dao.impl.ReviewImpl;
import model.Review;
import util.InputValidator;

import java.util.List;

public class ReviewService {
    private final ReviewDao reviewDao = new ReviewImpl();

    public void addReview(int userId, int rating, String comment) {

        try {
            if (!InputValidator.isRatingValid(rating)) {
                throw new IllegalArgumentException("Rating must be from 1 to 5.");
            }
            if (InputValidator.isNotBlank(comment)) {
                throw new IllegalArgumentException("Comment must not be empty.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot add review: " + e.getMessage(), e);
        }

    }

    public List<Review> getAllReviews() {
        try {
            return reviewDao.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get reviews: " + e.getMessage(), e);
        }
    }
}
