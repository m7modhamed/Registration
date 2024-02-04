package com.registration.registration;

import com.registration.User.IUserService;
import com.registration.User.User;
import com.registration.event.RegistrationCompleteEvent;
import com.registration.event.listener.RegistrationCompleteEventListener;
import com.registration.registration.password.IpasswordResetTokenService;
import com.registration.registration.token.VerificationToken;
import com.registration.registration.token.VerificationTokenService;
import com.registration.utility.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final VerificationTokenService tokenService;
    private final IUserService service;
    private final ApplicationEventPublisher publisher;
    private final RegistrationCompleteEventListener eventListener;
    private final IpasswordResetTokenService passwordResetTokenService;
    private final IUserService userService;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest, HttpServletRequest req) {
        Optional<User> user = service.findUserByEmail(registrationRequest.getEmail());
        if(user.isPresent() && user.get().getIsActive()){
            return "redirect:/registration/registration-form?exist";
        }else if(user.isPresent() && !user.get().getIsActive()){
            publisher.publishEvent(new RegistrationCompleteEvent(user.get(), UrlUtil.getApplicationUrl(req)));
            return "redirect:/registration/registration-form?notValid";
        }else{
            User theUser = service.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(theUser, UrlUtil.getApplicationUrl(req)));
            return "redirect:/registration/registration-form?success";
        }

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

    @GetMapping("/forgot-password-request")
    public String forgotPassword() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return "redirect:/registration/forgot-password-request?not_fond";
        }
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = UrlUtil.getApplicationUrl(request) + "/registration/password-reset-form?token=" + passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "password-reset-form";
    }


    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){

            return "redirect:/registration/forgot-password-request?invalid_token";
        }
        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()){
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        // add error tag in error.html for not found token
        return "redirect:/registration/forgot-password-request?not_found";
    }


}
