package com.example.tw_examen.controller;

import com.example.tw_examen.database.users.model.UserEntity;
import com.example.tw_examen.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String loadLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid email or password. Please try again.");
        }
        model.addAttribute("user", new UserEntity());
        return "authentication/login";
    }
}
