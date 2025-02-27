package com.chatbackend.dto;

import com.chatbackend.entity.Role;
import lombok.Data;
import java.util.Collection;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private Collection<Role> roles;
}