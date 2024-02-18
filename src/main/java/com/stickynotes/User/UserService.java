package com.stickynotes.User;

import com.stickynotes.registration.RegistrationRequest;
import com.stickynotes.registration.password.IpasswordResetTokenService;
import com.stickynotes.registration.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final IpasswordResetTokenService passwordTokenService;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest reg) {
        User user=new User(reg.getFirstName(),
                reg.getLastName(),
                reg.getEmail(),
                passwordEncoder.encode(reg.getPassword()),
        Arrays.asList(new Role("ROLE_ADMIN")));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail());

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        //theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        if(theUser.isPresent()){
            passwordTokenService.deleteUserToken(theUser.get().getId());
            verificationTokenService.deleteUserToken(theUser.get().getId());
        }
        userRepository.deleteById(id);
    }

}
