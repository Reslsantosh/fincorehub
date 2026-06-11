package com.resustainability.fincorehub.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resustainability.fincorehub.entity.Role;
import com.resustainability.fincorehub.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("""
        SELECT u FROM User u 
        WHERE (:searchTerm IS NULL OR :searchTerm = '' OR
            LOWER(u.username) LIKE LOWER(CONCAT(:searchTerm, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT(:searchTerm, '%')) OR
            LOWER(u.fullName) LIKE LOWER(CONCAT(:searchTerm, '%'))
        )
    """)
    Page<User> findAllUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Modifying
    @Query("""
        UPDATE User u SET
            u.username = :username,
            u.email = :email,
            u.fullName = :fullName,
            u.role = :role,
            u.active = :active,
            u.modifiedBy = :modifiedBy,
            u.modifiedOn = :modifiedOn
        WHERE u.id = :id
    """)
    int updateUser(
        @Param("id") Long id,
        @Param("username") String username,
        @Param("email") String email,
        @Param("fullName") String fullName,
        @Param("role") Role role,
        @Param("active") Boolean active,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedOn") LocalDateTime modifiedOn
    );
    
    @Modifying
    @Query("UPDATE User u SET u.active = :active, u.modifiedBy = :modifiedBy, u.modifiedOn = :modifiedOn WHERE u.id = :id")
    int updateUserActiveStatus(
        @Param("id") Long id,
        @Param("active") Boolean active,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedOn") LocalDateTime modifiedOn
    );
}
