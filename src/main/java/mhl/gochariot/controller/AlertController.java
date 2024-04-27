package mhl.gochariot.controller;

import jakarta.servlet.http.HttpServletRequest;
import mhl.gochariot.model.*;
import mhl.gochariot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AlertController {
    @Autowired
    UserService UserService;

    @Autowired
    RequestService requestService;

    @Autowired
    DriverService driverService;

    @Autowired
    AlertService alertService;

    @PostMapping("/api/alert/add")
    public ResponseEntity<?> addAlert(@ModelAttribute Alert alert) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = UserService.findByUsername(currentPrincipalName);

        alert.setUser(user);
        alert.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        alertService.saveAlert(alert);
        return ResponseEntity.ok("Alert successfully added");
    }
}
