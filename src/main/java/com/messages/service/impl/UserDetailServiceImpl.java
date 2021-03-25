package com.messages.service.impl;

import com.messages.entity.Role;
import com.messages.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;

public class UserDetailServiceImpl implements UserDetails { // Custom get user detail

    User user;

    public UserDetailServiceImpl(User user) {
        this.user = user;
    }

    public Integer getId() {
        return user.getId();
    }

    public String getFullName() {
        return user.getLastName() +" "+ user.getFirstName();
    }

    public String getUserImg() {
        return user.getUserImg();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Date getDateOfBirth() {
        return user.getDateOfBirth();
    }

    public String getGender() {
        return user.getGender();
    }

    public java.util.Date getCreatedAt() {
        return  user.getCreatedAt();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return true;
    }
}
