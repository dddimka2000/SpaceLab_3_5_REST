package org.example.security;

import jakarta.transaction.Transactional;
import org.example.entity.UserEntity;
import org.example.entity.UserRole;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {
    private final UserEntity userEntity;
    private Set<UserRole> roles;

    @Autowired
    public UserDetailsImpl(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.roles = userEntity.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @Override
    public String getPassword() {
        return userEntity.getPass();
    }

    @Override
    public String getUsername() {
        return userEntity.getLogin();
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

    public UserEntity getUserEntity() {
        return userEntity;
    }




}
