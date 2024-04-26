package mhl.gochariot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import mhl.gochariot.model.UserRole;
import mhl.gochariot.model.UserRoleKey;
import mhl.gochariot.model.User;
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

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    @GetMapping({"/home", "", "/index"})
    public String showHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                System.out.println("CURRENT ROEL: ");
                System.out.print(authority.getAuthority());
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

    @GetMapping("/signout")
    public String signout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/home";
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup";
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

    @GetMapping("/reviews")
    public String reviews() {
        return "reviews";
    }
}
