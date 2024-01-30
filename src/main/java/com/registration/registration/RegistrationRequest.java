package com.registration.registration;

import com.registration.User.Role;
import lombok.Data;
import java.util.Collection;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Collection<Role> roles;

}
