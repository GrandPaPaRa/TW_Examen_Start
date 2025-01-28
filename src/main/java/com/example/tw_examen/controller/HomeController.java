package com.example.tw_examen.controller;

import com.example.tw_examen.database.users.model.UserEntity;
import com.example.tw_examen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    @Autowired
   public HomeController(UserService userService){
        this.userService = userService;
   }

    @GetMapping()
    public String home(Model model) {
       UserEntity user = userService.getCurrentAuthenticatedUser();
       model.addAttribute("username", user.getUsername());
       return "user/home";
    }
}
