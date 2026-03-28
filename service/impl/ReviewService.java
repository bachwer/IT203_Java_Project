package service.impl;

import dao.ReviewDao;
import dao.impl.ReviewImpl;
import model.Review;
import service.ReviewInterface;
import util.InputValidator;

import java.util.List;

public class ReviewService implements ReviewInterface {
    private final ReviewDao reviewDao = new ReviewImpl();

    @Override
    public void addReview(int userId, int rating, String comment) {
        try {
            if (!InputValidator.isRatingValid(rating)) {
                throw new IllegalArgumentException("Rating must be from 1 to 5.");
            }

            if (InputValidator.isNotBlank(comment)) {
                throw new IllegalArgumentException("Comment must not be empty.");
            }

            Review review = new Review(0, userId, rating, comment, null);
            reviewDao.create(review);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot add review: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Review> getAllReviews() {
        try {
            return reviewDao.findAll();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get reviews: " + e.getMessage(), e);
        }
    }
}
