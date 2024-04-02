package mhl.gochariot.service;

import mhl.gochariot.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Review;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository ReviewRepository;

    public ReviewService() {

    }

    /**
     * Finds all reviews in the database
     *
     * @return the review list
     */
    public List getAllReviews() {
        return ReviewRepository.findAll();
    }

    /**
     *
     *
     * @param id
     * @return the specified review
     */
    public Review getReviewById(int id) {
        return ReviewRepository.findByReviewId(id);
    }

    /**
     * Saves a review to the review table
     *
     * @param review
     * @return the saved review
     */
    public Review saveReview(Review review) {
        return ReviewRepository.save(review);
    }

    /**
     * Deletes a specified review from the review table
     *
     * @param reviewId
     */
    public void deleteReview(int reviewId) {
        if (ReviewRepository.existsByReviewId(reviewId)) {
            ReviewRepository.deleteByReviewId(reviewId);
        } else {
            // TODO
        }}

    }
