package com.resustainability.fincorehub.request;

import com.resustainability.fincorehub.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
    @NotBlank(message = "Username is required")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    @NotBlank(message = "Full name is required")
    String fullName,
    
    @NotNull(message = "Role is required")
    Role role,
    
    @NotNull(message = "Active status is required")
    Boolean active
) {}
