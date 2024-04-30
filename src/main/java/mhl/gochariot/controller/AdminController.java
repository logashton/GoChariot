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
@RequestMapping("/admin")
public class AdminController {
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
    public String adminHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByUsername(currentPrincipalName);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "admin/adm_index";
    }

    @GetMapping("/manage")
    public String adminManage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/adm_manage";
    }

    @GetMapping("/verify")
    public String adminVerify(Model model) {
        System.out.println(driverService.findAllUnverified().toString());
        model.addAttribute("drivers", driverService.findAllUnverified());
        return "admin/adm_verify";
    }

    @GetMapping("/alert")
    public String adminVerify() {
        return "admin/adm_alert";
    }
}
