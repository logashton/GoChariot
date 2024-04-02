package mhl.gochariot.repository;

import mhl.gochariot.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findByReviewId(int reviewId);
    boolean existsByReviewId(int reviewId);
    void deleteByReviewId(int reviewId);
}