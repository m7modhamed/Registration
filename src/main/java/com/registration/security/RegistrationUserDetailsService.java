package com.registration.security;

import com.registration.User.Role;
import com.registration.User.User;
import com.registration.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegistrationUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repo.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        String userName = user.get().getEmail();
        String password = user.get().getPassword();
        boolean isActive = user.get().getIsActive();

        List<GrantedAuthority> roles = user.get().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());


        return new RegistrationUserDetails(userName, password, isActive, roles);
    }
}
