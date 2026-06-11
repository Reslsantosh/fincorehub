package com.resustainability.fincorehub.controller;

import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.request.AddUserRequest;
import com.resustainability.fincorehub.request.UpdateUserRequest;
import com.resustainability.fincorehub.response.UserResponse;
import com.resustainability.fincorehub.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public APIResponse<UserResponse> addUser(@Valid @RequestBody AddUserRequest request) {
        return new APIResponse<>(
                "User created successfully",
                userService.addUser(request)
        );
    }

    @GetMapping("/list")
    public APIResponse<Pager<UserResponse>> list(@ModelAttribute SearchCriteria searchCriteria) {
        return new APIResponse<>(
                userService.list(searchCriteria)
        );
    }

    @GetMapping("/details/{id}")
    public APIResponse<UserResponse> getById(@PathVariable Long id) {
        return new APIResponse<>(
                "User details fetched successfully",
                userService.getById(id)
        );
    }

    @PutMapping("/update/{id}")
    public APIResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return new APIResponse<>(
                "User updated successfully",
                userService.updateUser(id, request)
        );
    }

    @PostMapping("/{id}/toggle-active")
    public APIResponse<Void> toggleActive(
            @PathVariable Long id,
            @RequestParam boolean active) {
        userService.toggleUserActive(id, active);
        return new APIResponse<>(
                active ? "User activated successfully" : "User deactivated successfully"
        );
    }

    @PostMapping("/{id}/reset-password")
    public APIResponse<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return new APIResponse<>(
                "Password reset successfully"
        );
    }
}
