package mhl.gochariot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import mhl.gochariot.model.Student;

@Controller
@RequestMapping("/student")
public class StudentController {
    @GetMapping({"/", "/home", "/index", ""})
    public String userHome() {
        return "student/stu_index";
    }
}
