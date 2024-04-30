package mhl.gochariot.controller;

import jakarta.servlet.http.HttpServletRequest;
import mhl.gochariot.model.Driver;
import mhl.gochariot.model.DriverName;
import mhl.gochariot.model.User;
import mhl.gochariot.service.DriverNameService;
import mhl.gochariot.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import mhl.gochariot.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    DriverService driverService;

    @Autowired
    DriverNameService driverNameService;



    // modelattribute isn't necessary here but makes my life easier tbh
    // username, firstName, lastName, email, accountNonLocked
    @PostMapping("/api/users/edit")
    public ResponseEntity<?> editUser(@ModelAttribute User user) {
        User targetUser = userService.findById(user.getUserId());
        System.out.println("trying to edit: ");
        System.out.print(targetUser.toString());
        System.out.println("so far: ");
        System.out.print(user.toString());


        targetUser.setUsername(user.getUsername());
        targetUser.setFirstName(user.getFirstName());
        targetUser.setLastName(user.getLastName());
        targetUser.setEmail(user.getEmail());
        targetUser.setAccountNonLocked(user.isAccountNonLocked());
        System.out.println("\nafter user edit: ");
        System.out.print(targetUser.toString());

        try {
            userService.saveUser(targetUser);
            return ResponseEntity.ok("User successfully edited");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Bad user edit");
        }

    }

    @PostMapping("/api/users/verify")
    public ResponseEntity<?> verifyUser(@ModelAttribute User user, HttpServletRequest data) {
        User targetUser = userService.findById(user.getUserId());
        Integer driverIdPGO = Integer.parseInt(data.getParameter("driverIdPGO"));
        targetUser.setAccountNonLocked(true);

        try {
            userService.saveUser(targetUser);
            return ResponseEntity.ok("Driver successfully verified");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Failed to verify driver");
        }
    }

    @PostMapping("/api/users/reject")
    public ResponseEntity<?> rejectUser(@ModelAttribute User user, HttpServletRequest data) {
        User targetUser = userService.findById(user.getUserId());
        Integer driverIdPGO = Integer.parseInt(data.getParameter("driverIdPGO"));
        Driver targetDriver = driverService.findByDriverIdPGO(driverIdPGO).get();
        DriverName driverName = driverNameService.findByDriverIdPGO(driverIdPGO).get();

        try {
            driverService.deleteDriver(targetDriver);
            driverNameService.deleteDriverName(driverName);
            return ResponseEntity.ok("Driver successfully rejected");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Failed to rejected driver");
        }
    }


    /*
    @GetMapping({"/all", "/", ""})
    public String getAllUsers(Model model) {
        model.addAttribute("userList",
                //UserService.getAllUsers());
        return "user/user-list";
    }

    @GetMapping("/id={userId}")
    public String getUserById(@PathVariable int userId, Model model) {
        model.addAttribute("user",
                //UserService.getUserById(userId));
        return "user/user-detail";
    }*/

}
