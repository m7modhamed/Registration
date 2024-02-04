package com.registration.home;

import com.registration.User.User;
import com.registration.User.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository repo;

    @GetMapping("")
    public String main() {
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest http) {
        return "login";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
