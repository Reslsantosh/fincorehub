package com.resustainability.fincorehub.service;

import com.resustainability.fincorehub.config.SecurityUtils;
import com.resustainability.fincorehub.entity.Role;
import com.resustainability.fincorehub.entity.User;
import com.resustainability.fincorehub.exception.DataAlreadyExistException;
import com.resustainability.fincorehub.exception.InvalidDataException;
import com.resustainability.fincorehub.exception.ResourceNotFoundException;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.repository.UserRepository;
import com.resustainability.fincorehub.request.AddUserRequest;
import com.resustainability.fincorehub.request.UpdateUserRequest;
import com.resustainability.fincorehub.response.UserResponse;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserResponse addUser(AddUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DataAlreadyExistException("Username already exists: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DataAlreadyExistException("Email already exists: " + request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setRole(request.role());
        user.setActive(true);

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Pager<UserResponse> list(SearchCriteria searchCriteria) {
        Page<User> page = userRepository.findAllUsers(
                searchCriteria.getQ(),
                searchCriteria.toPageRequest()
        );
        return Pager.of(page.map(this::mapToResponse));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existing.getUsername().equals(request.username()) && 
            userRepository.existsByUsername(request.username())) {
            throw new DataAlreadyExistException("Username already exists: " + request.username());
        }

        if (!existing.getEmail().equals(request.email()) && 
            userRepository.existsByEmail(request.email())) {
            throw new DataAlreadyExistException("Email already exists: " + request.email());
        }

        String currentUser = SecurityUtils.getCurrentUser();
        if (existing.getUsername().equals(currentUser) && !request.active()) {
            throw new InvalidDataException("You cannot deactivate your own account");
        }

        existing.setUsername(request.username());
        existing.setEmail(request.email());
        existing.setFullName(request.fullName());
        existing.setRole(request.role());
        existing.setActive(request.active());

        User updated = userRepository.save(existing);
        return mapToResponse(updated);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void toggleUserActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        String currentUser = SecurityUtils.getCurrentUser();
        
        if (user.getUsername().equals(currentUser) && !active) {
            throw new InvalidDataException("You cannot deactivate your own account");
        }

        int updated = userRepository.updateUserActiveStatus(
                id, 
                active, 
                currentUser, 
                LocalDateTime.now()
        );

        if (updated == 0) {
            throw new InvalidDataException("Failed to update user status");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getActive(),
                user.getCreatedBy(),
                user.getCreatedOn(),
                user.getModifiedBy(),
                user.getModifiedOn()
        );
    }
}
