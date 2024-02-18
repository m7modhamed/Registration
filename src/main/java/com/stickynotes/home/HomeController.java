package com.stickynotes.home;

import com.stickynotes.User.IUserService;
import com.stickynotes.User.User;
import com.stickynotes.User.UserRepository;
import com.stickynotes.notes.InoteService;
import com.stickynotes.notes.Note;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final InoteService noteService;
    private final IUserService userService;
    private final UserRepository repo;

    @GetMapping("")
    public String main(Model model,HttpServletRequest http) {
        Optional<User> user= getCurrentUser(http);
        List<Note> notes=null;
        if (user.isPresent()) {
            notes=user.get().getNotes();
        }
        model.addAttribute("notes",notes) ;
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

    private Optional<User> getCurrentUser(HttpServletRequest http){
        Principal principal = http.getUserPrincipal();
        String email = principal.getName();
        Optional<User> user = userService.findUserByEmail(email);
        return user;
    }
}
