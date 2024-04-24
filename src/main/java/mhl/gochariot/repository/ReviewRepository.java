package mhl.gochariot.repository;

import mhl.gochariot.model.Review;
import mhl.gochariot.service.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findById(int reviewId);
    boolean existsById(int reviewId);
    void deleteById(int reviewId);
    @Query("SELECT new mhl.gochariot.service.ReviewDTO(r.id, u.firstName, u.lastName, r.driverFirstName, r.driverLastName, dn.driverIdPGO, u.username, r.rating, r.content, r.createdAt) FROM Review r JOIN r.user u JOIN r.driverIdPGO dn")
    Page<ReviewDTO> findAllReviewsWithUserNames(Pageable pageable);

    @Query("SELECT new mhl.gochariot.service.ReviewDTO(r.id, u.firstName, u.lastName, r.driverFirstName, r.driverLastName, dn.driverIdPGO, u.username, r.rating, r.content, r.createdAt) FROM Review r JOIN r.user u JOIN r.driverIdPGO dn WHERE r.driverFirstName = ?1 AND r.driverLastName = ?2")
    Page<ReviewDTO> findReviewsByDriverName(String firstName, String lastName, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.driverFirstName = :driverFirstName AND r.driverLastName = :driverLastName")
    Optional<Double> avgRatingByFirstNameAndLastName(@Param("driverFirstName") String driverFirstName, @Param("driverLastName") String driverLastName);

    @Query("SELECT new mhl.gochariot.service.ReviewDTO(r.id, u.firstName, u.lastName, r.driverFirstName, r.driverLastName, dn.driverIdPGO, u.username, r.rating, r.content, r.createdAt) FROM Review r JOIN r.user u JOIN r.driverIdPGO dn WHERE dn.driverIdPGO = ?1")
    Page<ReviewDTO> findReviewByDriverIdPGO(Integer id, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.driverIdPGO = ?1")
    Optional<Double> avgRatingByDriverIdPGO(Integer id);
}