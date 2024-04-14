package mhl.gochariot.controller;

import mhl.gochariot.service.DriverNameDTO;
import mhl.gochariot.service.DriverNameService;
import mhl.gochariot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
@RequestMapping("/")
public class PublicController {
    @Autowired
    ReviewService reviewService;

    @Autowired
    DriverNameService driverNameService;

    @GetMapping({"/home", "", "/index"})
    public String showHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("Student")) {
                    return "redirect:/student";
                }
            }
        }
        return "index";
    }
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/reviews")
    public String reviews() {
        return "reviews";
    }
}
