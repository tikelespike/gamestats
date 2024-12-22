package com.tikelespike.gamestats.businesslogic.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class User implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Set<UserRole> roles;

    public User(String email, String password, Set<UserRole> roles) {
        this(null, email, password, roles);
    }

    public User(Long id, String email, String password, Set<UserRole> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.toString())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Set<UserRole> getRoles() {
        return new HashSet<>(roles);
    }
}
