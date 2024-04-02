package mhl.gochariot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import mhl.gochariot.service.ReviewService;
import mhl.gochariot.model.Review;


@Controller
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    ReviewService ReviewService;
    
    @GetMapping({"/all", "/", ""})
    public String getAllReviews() {
        ReviewService.getAllReviews();
        return "TODO";
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
