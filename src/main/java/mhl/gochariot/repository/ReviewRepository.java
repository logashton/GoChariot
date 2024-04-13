package mhl.gochariot.repository;

import mhl.gochariot.model.Review;
import mhl.gochariot.service.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findById(int reviewId);
    boolean existsById(int reviewId);
    void deleteById(int reviewId);
    @Query("SELECT new mhl.gochariot.service.ReviewDTO(r.id, u.firstName, u.lastName, u.username, r.rating, r.content, r.createdAt) FROM Review r JOIN r.user u")
    Page<ReviewDTO> findAllReviewsWithUserNames(Pageable pageable);
}