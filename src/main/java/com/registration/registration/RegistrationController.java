package com.registration.registration;

import com.registration.User.IUserService;
import com.registration.User.User;
import com.registration.event.RegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final IUserService service;
    private final ApplicationEventPublisher publisher;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new RegistrationRequest());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest){
        User user=service.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user,""));
        return "redirect:/registration/registration-form?success";
    }
}
