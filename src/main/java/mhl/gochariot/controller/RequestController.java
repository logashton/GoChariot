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

    @GetMapping("/api/requests/driver_requests")
    public Page<RequestDTO> requestsByDriver(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "size", defaultValue = "4") Integer pageSize,
            @RequestParam(name = "status") String status
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = UserService.findByUsername(currentPrincipalName);
        Driver driver = driverService.findByUser(user).get();

        return requestService.getRequestsByDriverIdAndStatus(driver.getDriverId(), status, pageNo, pageSize);
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
        System.out.println("does it even make it here???");
        status = status.toLowerCase();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = UserService.findByUsername(currentPrincipalName);
        String role = "";

        System.out.println("FOUND USER WITH NAME: ");
        System.out.println(user.getUsername());
        System.out.println(user.toString());
        System.out.println(user.getRoles().toString());

        System.out.println("Authenticated user: " + authentication.getName());
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            role = authority.getAuthority();
        }

        System.out.println("SENDING REQUEST STATUS UPATE WITH ROLE");
        System.out.println(role);
        System.out.println("\n\n\n");


        Optional<Request> request = requestService.findById(id);

        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (!VALID_STATUSES.contains(status)) {
            System.out.println("invalid status");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status");
        } else if (Objects.equals(role, "Student")) {
            if (!Objects.equals(request.get().getUser().getUserId(), user.getUserId())) {
                System.out.println("user tried to edit request they don't own\n\n\n");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You do not own this request");
            }

            if (status.equals("declined") || status.equals("accepted")) {
                System.out.println("user tried to set a status theyr'e nto allowed to \n\n\n");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("You do not have permission to set this status");
            }
        } else if (Objects.equals(role, "Driver")) {
            Driver requestDriver = request.get().getDriver();
            Optional<Driver> driverUser = driverService.findByUser(user);
            System.out.println(requestDriver.getUser().getUserId());
            System.out.println(driverUser.get().getUser().getUserId());
            if (!Objects.equals(requestDriver.getUser().getUserId(), driverUser.get().getUser().getUserId())) {
                System.out.println("driver tried to edit request they don't own\n\n\n");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You are not the driver of this request");
            }
        }

        requestService.updateRequestStatus(id, status);
        return ResponseEntity.ok("Status updated");

    }


}
