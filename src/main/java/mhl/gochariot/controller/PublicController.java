package mhl.gochariot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/")
public class PublicController {
    // todo: authentication redirects
    @GetMapping({"/home", ""})
    public String showHome() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // redirects based on user role
        if (auth != null) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT"))) {
                return "error";
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DRIVER"))) {
                return "";
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
