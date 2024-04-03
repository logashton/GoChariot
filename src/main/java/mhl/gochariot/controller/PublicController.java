package mhl.gochariot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/")
public class PublicController {
    // todo: authentication redirects
    @GetMapping({"/home", ""})
    public String showHome() {
        return "index";
    }
}
