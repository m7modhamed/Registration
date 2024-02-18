package com.stickynotes.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;


    @GetMapping("/")
    public String getUsers(Model model) {
        model.addAttribute("users", service.getAllUsers());
        return "users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = service.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "update-user";
        } else {
            return "redirect:/users/?notfound";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user) {

        if (user != null) {
            service.updateUser(user);
            return "redirect:/users/?update_Success";
        } else {
            return "redirect:/users/?notfound";
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return "redirect:/users/?delete_success";
    }

}
