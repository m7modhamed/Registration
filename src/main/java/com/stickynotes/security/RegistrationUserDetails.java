package com.stickynotes.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class RegistrationUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean isActive;
    private List<GrantedAuthority> roles;

   /* public RegistrationUserDetails(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.isActive = user.getIsActive();
        this.roles = Arrays.stream(user.getRoles().toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }*/
   public RegistrationUserDetails(String userName,String password ,boolean isActive,List<GrantedAuthority> roles) {
       this.userName = userName;
       this.password = password;
       this.isActive = isActive;
       this.roles = roles;
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
