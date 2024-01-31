package com.registration.registration;

import com.registration.User.IUserService;
import com.registration.User.User;
import com.registration.event.RegistrationCompleteEvent;
import com.registration.registration.token.VerificationToken;
import com.registration.registration.token.VerificationTokenService;
import com.registration.utility.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final VerificationTokenService tokenService;
    private final IUserService service;
    private final ApplicationEventPublisher publisher;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new RegistrationRequest());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest, HttpServletRequest req){
        User user=service.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(req)));
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().getIsActive()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
}
