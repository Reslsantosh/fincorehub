package com.resustainability.fincorehub.response;

import com.resustainability.fincorehub.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String username,
    String email,
    String fullName,
    Role role,
    Boolean active,
    String createdBy,
    LocalDateTime createdOn,
    String modifiedBy,
    LocalDateTime modifiedOn
) {}
