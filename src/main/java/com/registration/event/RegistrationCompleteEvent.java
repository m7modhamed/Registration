package com.registration.event;

import com.registration.User.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String confirmationUrl;
    public RegistrationCompleteEvent(User user, String confirmationUrl) {
        super(user);
        this.user=user;
        this.confirmationUrl=confirmationUrl;
    }
}
