package com.testkiller.backend.repository;

import com.testkiller.backend.entity.Role;
import com.testkiller.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);
}
