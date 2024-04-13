package mhl.gochariot.controller;

import mhl.gochariot.service.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import mhl.gochariot.service.ReviewService;
import mhl.gochariot.model.Review;

import java.util.List;


@RestController
public class ReviewController {
    @Autowired
    ReviewService ReviewService;
    
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
    
    @GetMapping("/create")
    public String createReview(Review review) {
        ReviewService.saveReview(review);
        return "TODO";
    }
    
    @GetMapping("/delete/id={id}")
    public String deleteReview(@PathVariable int id) {
        ReviewService.deleteReview(id);
        return "TODO";
    }
}
