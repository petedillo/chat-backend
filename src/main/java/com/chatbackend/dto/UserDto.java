package com.chatbackend.dto;

import com.chatbackend.entity.Role;
import lombok.Data;
import java.util.Collection;


@Data
public class UserDto {
    private String username;
    private String password;
    private Collection<Role> roles;
}