package com.example.tw_examen.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
    @GetMapping()
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        int statusCode = response.getStatus(); // Get status code from response

        if (statusCode == 404)
            return "error/missing";

        if (statusCode == 403)
            return "error/access";

        return "error/error"; // Default error page
    }
}
