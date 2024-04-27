package mhl.gochariot.controller;

import mhl.gochariot.model.User;
import mhl.gochariot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import mhl.gochariot.model.Review;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class RequestController {
    @Autowired
    UserService UserService;

    @Autowired
    RequestService requestService;

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



}
