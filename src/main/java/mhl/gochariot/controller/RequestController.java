package mhl.gochariot.controller;

import jakarta.servlet.http.HttpServletRequest;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.Request;
import mhl.gochariot.model.User;
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
import mhl.gochariot.model.Review;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class RequestController {
    @Autowired
    UserService UserService;

    @Autowired
    RequestService requestService;

    @Autowired
    DriverService driverService;

    static final List<String> VALID_STATUSES = Arrays.asList("pending", "accepted", "declined", "canceled");

    @GetMapping("/api/requests/user_requests")
    public Page<RequestDTO> requestsByUser(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "size", defaultValue = "4") Integer pageSize
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = UserService.findByUsername(currentPrincipalName);

        return requestService.getRequestsByUserId(user.getUserId(), pageNo, pageSize);
    }

    @PostMapping("/api/requests/add")
    public ResponseEntity<?> addRequest(@ModelAttribute Request request, HttpServletRequest data) {
        System.out.println("Request received");
        System.out.println(request.toString());


        Integer driverIdPGO = Integer.parseInt(data.getParameter("driverIdPGO"));
        System.out.println(driverIdPGO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = UserService.findByUsername(currentPrincipalName);
        Driver driver = driverService.findByDriverIdPGO(driverIdPGO).get();
        System.out.println(driver.toString());

        request.setUser(user);
        request.setDriver(driver);
        request.setStatus("pending");
        request.setRequestTime(new Timestamp(System.currentTimeMillis()));

        boolean alreadyRequested = requestService.driverAlreadyRequested(user, driver);
        System.out.println(alreadyRequested);

        if (alreadyRequested) {
            return ResponseEntity.ok("You already have a pending ride request to this driver");
        } else {
            requestService.saveRequest(request);
            return ResponseEntity.ok("Ride request sent");
        }

    }

    @PostMapping("/api/requests/update_status")
    public ResponseEntity<?> updateStatus(
        @RequestParam(name = "id") Integer id,
        @RequestParam(name = "status") String status
     ) {
        status = status.toLowerCase();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = UserService.findByUsername(currentPrincipalName);
        String role = "";

        System.out.println("Authenticated user: " + authentication.getName());
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            role = authority.getAuthority();
        }


        Optional<Request> request = requestService.findById(id);

        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (!VALID_STATUSES.contains(status)) {
            System.out.println("invalid status");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
        } else if (role == "student") {
            if (!Objects.equals(request.get().getUser().getUserId(), user.getUserId())) {
                System.out.println("forbidden, user tried to edit request they don't own");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this request");
            }

            if (status == "declined" || status == "accepted") {
                System.out.println("forbidden, user tried to set a status theyr'e nto allowed to");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to set this status");
            }
        } else if (role == "driver") {
                Driver requestDriver = request.get().getDriver();
                Optional<Driver> driverUser = driverService.findByUser(user);

                if (!Objects.equals(requestDriver.getUser().getUserId(), driverUser.get().getUser().getUserId())) {
                    System.out.println("forbidden, driver tried to edit request they don't own");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the driver of this request");
                }
        }

        requestService.updateRequestStatus(id, status);
        return ResponseEntity.ok("Status updated");


    }


}
