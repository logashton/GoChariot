package mhl.gochariot.controller;

import mhl.gochariot.service.DriverNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    DriverNameService driverNameService;

    @GetMapping({"/", "/home", "/index", ""})
    public String studentHome() {
        return "student/stu_index";
    }

    @GetMapping("/review")
    public String studentReview(Model model) {
        model.addAttribute("driverNameList",
                driverNameService.findAllDriverNames());
        return "student/stu_reviews";
    }

    @GetMapping("/add-review")
    public String studentAddReview(Model model) {
        model.addAttribute("driverNameList",
                driverNameService.findAllDriverNames());
        return "student/stu_add_review";
    }

    @GetMapping("/tracker")
    public String studentTracker() {
        return "student/stu_track";
    }
}
