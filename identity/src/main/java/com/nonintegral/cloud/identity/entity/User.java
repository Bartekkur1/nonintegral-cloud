package com.nonintegral.cloud.identity.entity;

import com.nonintegral.cloud.identity.entity.request.LoginRequest;
import com.nonintegral.cloud.identity.entity.request.RegisterRequest;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Document
public class User extends Credentials implements UserDetails {

    @Id
    private String id;
    private Date createdAt;
    private Date lastLogin;
    private Boolean active;
    private String utilToken;
    private List<UserRole> roles;

    public UserDetails asUserDetails() {
        return this;
    }

    public User() {}

    public User(Credentials credentials) {
        this.roles = new ArrayList<>();
        this.roles.add(UserRole.ROLE_USER);
        this.email = credentials.getEmail();
        this.password = credentials.getPassword();

        if(credentials instanceof RegisterRequest) {
            this.createdAt = new Date();
            this.active = false;
        } else if(credentials instanceof LoginRequest) {
            this.lastLogin = new Date();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return this.active;
    }
}
