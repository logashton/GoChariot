package mhl.gochariot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import mhl.gochariot.model.*;
import mhl.gochariot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PublicController {
    @Autowired
    ReviewService reviewService;

    @Autowired
    DriverNameService driverNameService;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    DriverService driverService;


    @GetMapping({"/home", "", "/index"})
    public String showHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                System.out.println("CURRENT ROEL: ");
                System.out.print(authority.getAuthority());
                if (authority.getAuthority().equals("Student")) {
                    System.out.println("redirecting because student");
                    return "redirect:/student";
                } else if (authority.getAuthority().equals("Driver")) {
                    System.out.println("redirecting because driver");
                    return "redirect:/driver";
                } else if (authority.getAuthority().equals("Admin")) {
                    System.out.println("redirecitng becasue admin");
                    return "redirect:/admin";
                }
            }
        }
        System.out.println("no redirect logged in");
        return "index";
    }
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signout")
    public String signout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/home";
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @GetMapping("/driver_signup")
    public String driverSignup() {
        return "driver_signup";
    }

    @PostMapping("/signup")
    public String postSignup(
            @NonNull @RequestParam("firstName") String firstName,
            @NonNull @RequestParam("lastName") String lastName,
            @NonNull @RequestParam("email") String email,
            @NonNull @RequestParam("username") String username,
            @NonNull @RequestParam("password") String password,
            @NonNull @RequestParam("confirmPassword") String confirmPassword,
            Model model
            ) {
        System.out.printf("%s %s %s %s %s", firstName, lastName, email, username, password);

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            model.addAttribute("error", "Username does not match required format");
        } else if (password.length() < 8) {
            //model.addAttribute("error", "Password too short");
        } else if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username taken");
        } else if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "Email already registered");
        } else if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
        } else {

            String hashedPassword = passwordEncoder.encode(password);
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUsername(username);
            user.setPasswordHash(hashedPassword);
            user.setPasswordSalt("123");
            user.setEnabled(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            userService.saveUser(user);

            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUserId(user.getUserId());
            userRoleKey.setRoleId(1);


            UserRole userRole = new UserRole();
            userRole.setId(userRoleKey);
            userRole.setUser(user);;
            userRole.setRole(roleService.getRole(1));
            userRoleService.saveUserRole(userRole);

            model.addAttribute("success", "Successfully signed up!");
            return "redirect:/login";

        }


        return "signup";

    }

    @PostMapping("/driver_signup")
    public String postDriverSignup(
            @NonNull @RequestParam("firstName") String firstName,
            @NonNull @RequestParam("lastName") String lastName,
            @NonNull @RequestParam("email") String email,
            @NonNull @RequestParam("username") String username,
            @NonNull @RequestParam("password") String password,
            @NonNull @RequestParam("confirmPassword") String confirmPassword,
            @NonNull @RequestParam("driverIdPGO") Integer driverIdPGO,
            Model model
    ) {
        System.out.printf("%s %s %s %s %s", firstName, lastName, email, username, password, driverIdPGO);

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            model.addAttribute("error", "Username does not match required format");
        } else if (password.length() < 8) {
            //model.addAttribute("error", "Password too short");
        } else if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username taken");
        } else if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "Email already registered");
        } else if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
        } else {

            String hashedPassword = passwordEncoder.encode(password);
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUsername(username);
            user.setPasswordHash(hashedPassword);
            user.setPasswordSalt("123");
            user.setEnabled(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            user.setAccountNonExpired(true);
            // false becasue they w ill ahve ot be contacted for manual verification
            user.setAccountNonLocked(false);
            user.setCredentialsNonExpired(true);
            userService.saveUser(user);

            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUserId(user.getUserId());
            userRoleKey.setRoleId(2);


            UserRole userRole = new UserRole();
            userRole.setId(userRoleKey);
            userRole.setUser(user);;
            userRole.setRole(roleService.getRole(2));
            userRoleService.saveUserRole(userRole);

            Driver driver = new Driver();
            driver.setUser(user);

            Optional<DriverName> opDn = driverNameService.findByDriverIdPGO(driverIdPGO);

            if (opDn.isEmpty()) {
                DriverName dn = new DriverName();
                dn.setDriverIdPGO(driverIdPGO);
                dn.setFirstName(firstName);
                dn.setLastName(lastName);
                dn.setLastSeen(new Timestamp(System.currentTimeMillis()));
                dn.setFirstSeen(new Timestamp(System.currentTimeMillis()));
                driverNameService.saveDriverName(dn);
                driver.setDriverIdPGO(driverNameService.findByDriverIdPGO(driverIdPGO).get());
                driver.setHoursClocked(0);
                driver.setRides(0);
                driverService.saveDriver(driver);
            } else {
                driver.setDriverIdPGO(opDn.get());
                driver.setHoursClocked(0);
                driver.setRides(0);
                driverService.saveDriver(driver);
            }



            model.addAttribute("success", "Successfully signed up! Please wait for an admin to contact you for verification.");

        }


        return "driver_signup";

    }

    @GetMapping("/reviews")
    public String reviews() {
        return "reviews";
    }
}
