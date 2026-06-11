package com.resustainability.fincorehub.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.resustainability.fincorehub.config.SecurityUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    public User() {

    }

    public User(Long id, String username, String password, String email, String fullName, 
            Role role, Boolean active, String createdBy, String modifiedBy, 
            LocalDateTime createdOn, LocalDateTime modifiedOn) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @PrePersist
    public void onCreate() {
        String user = SecurityUtils.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        this.createdBy = user;
        this.createdOn = now;

        this.modifiedBy = null;
        this.modifiedOn = null;
    }
    
    @PreUpdate
    public void onUpdate() {
        this.modifiedBy = SecurityUtils.getCurrentUser();
        this.modifiedOn = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", fullName=" + fullName
                + ", role=" + role + ", active=" + active + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
                + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, createdBy, createdOn, email, fullName, id, modifiedBy, modifiedOn, password,
                role, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(active, other.active) && Objects.equals(createdBy, other.createdBy)
                && Objects.equals(createdOn, other.createdOn) && Objects.equals(email, other.email)
                && Objects.equals(fullName, other.fullName) && Objects.equals(id, other.id)
                && Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(modifiedOn, other.modifiedOn)
                && Objects.equals(password, other.password) && Objects.equals(role, other.role)
                && Objects.equals(username, other.username);
    }
}
