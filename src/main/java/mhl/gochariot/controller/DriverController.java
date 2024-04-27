package mhl.gochariot.controller;

import mhl.gochariot.model.Alert;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.User;
import mhl.gochariot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    DriverNameService driverNameService;

    @Autowired
    UserService userService;

    @Autowired
    AlertService alertService;

    @Autowired
    RequestService requestService;

    @Autowired
    DriverService driverService;

    @Autowired
    ReviewService reviewService;

    @GetMapping({"/", "/home", "/index", ""})
    public String driverHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByUsername(currentPrincipalName);
        Driver driver = driverService.findByUser(user).get();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        String average = "No reviews";
        Optional<Double> optAverage = reviewService.avgReviewsByDriverIdPGO(driver.getDriverIdPGO().getDriverIdPGO());

        if (optAverage.isPresent()) {
            average = "★★★★★".substring(0, (int)Math.round(optAverage.get()));
        }

        model.addAttribute("rating", average);

        return "driver/dri_index";
    }

    @GetMapping("/requests")
    public String driverRequests() {
        return "driver/dri_requests";
    }

    @GetMapping("/alert")
    public String driverAlert() {
        return "driver/dri_alert";
    }
}
