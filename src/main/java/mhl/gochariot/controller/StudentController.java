package mhl.gochariot.controller;

import mhl.gochariot.model.Alert;
import mhl.gochariot.model.User;
import mhl.gochariot.service.AlertService;
import mhl.gochariot.service.DriverNameService;
import mhl.gochariot.service.RequestService;
import mhl.gochariot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    DriverNameService driverNameService;

    @Autowired
    UserService userService;

    @Autowired
    AlertService alertService;

    @Autowired
    RequestService requestService;


    @GetMapping({"/", "/home", "/index", ""})
    public String studentHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByUsername(currentPrincipalName);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        List<Alert> alerts = alertService.findAllAlerts();
        model.addAttribute("alerts", alerts);
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

    @GetMapping("/requests")
    public String studentRequests(Model model) {
        return "student/stu_requests";
    }
}
