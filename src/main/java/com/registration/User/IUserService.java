package com.registration.User;

import com.registration.registration.RegistrationRequest;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUsers();

    User registerUser(RegistrationRequest registrationRequest);

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);
}
