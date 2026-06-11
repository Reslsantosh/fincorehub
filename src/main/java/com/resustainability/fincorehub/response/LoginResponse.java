package com.resustainability.fincorehub.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response with user details")
public record LoginResponse(
    @Schema(description = "User email")
    String email,
    
    @Schema(description = "User display name")
    String name,
    
    @Schema(description = "Login success message")
    String message
) {}
