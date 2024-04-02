package mhl.gochariot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import mhl.gochariot.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService UserService;

    @GetMapping({"/all", "/", ""})
    public String getAllUsers(Model model) {
        model.addAttribute("userList",
                UserService.getAllUsers());
        return "user/user-list";
    }

    @GetMapping("/id={userId}")
    public String getUserById(@PathVariable int userId, Model model) {
        model.addAttribute("user",
                UserService.getUserById(userId));
        return "user/user-detail";
    }

}
