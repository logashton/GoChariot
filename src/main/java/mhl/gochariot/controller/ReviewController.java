package mhl.gochariot.controller;

import mhl.gochariot.model.User;
import mhl.gochariot.service.ReviewDTO;
import mhl.gochariot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import mhl.gochariot.service.ReviewService;
import mhl.gochariot.model.Review;
import java.sql.Timestamp;


@RestController
public class ReviewController {
    @Autowired
    ReviewService ReviewService;

    @Autowired
    UserService UserService;

    @GetMapping({"/api/reviews/all", "/api/reviews/", "/api/reviews"})
    public Page<ReviewDTO> getAllReviews(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {

        return ReviewService.getAllReviews(pageNo, pageSize);
    }
    
    @GetMapping("/id={id}")
    public String getReviewById(@PathVariable int id) {
        ReviewService.getReviewById(id);
        return "TODO";
    }
    
    @PostMapping("/api/reviews/add")
    public ResponseEntity<String> createReview(@ModelAttribute Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = UserService.findByUsername(currentPrincipalName);
        review.setUser(user);
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Review savedReview = ReviewService.saveReview(review);

        if (savedReview != null) {
            return ResponseEntity.ok("Review successfully added");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review");
        }
    }
    
    @PostMapping("/delete/id={id}")
    public String deleteReview(@PathVariable int id) {
        ReviewService.deleteReview(id);
        return "TODO";
    }
}
