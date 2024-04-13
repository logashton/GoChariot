package mhl.gochariot.service;

import mhl.gochariot.repository.ReviewRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mhl.gochariot.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository ReviewRepository;

    public ReviewService() {

    }

    /**
     * Finds all reviews in the database with pagination
     *
     * @return the review list
     */
    public Page<ReviewDTO> getAllReviews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return ReviewRepository.findAllReviewsWithUserNames(pageable);

    }

    /**
     *
     *
     * @param id
     * @return the specified review
     */
    public Review getReviewById(int id) {
        return ReviewRepository.findById(id);
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
        if (ReviewRepository.existsById(reviewId)) {
            ReviewRepository.deleteById(reviewId);
        } else {
            // TODO
        }}

    }
