package com.nonintegral.cloud.identity.dto;

import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.UserRole;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {

    private String id;
    private String email;
    private Date createdAt;
    private Date lastLogin;
    private Boolean active;
    private List<UserRole> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.lastLogin = user.getLastLogin();
        this.active = user.getActive();
        this.roles = user.getRoles();
    }
}
