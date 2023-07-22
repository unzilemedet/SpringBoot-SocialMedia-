package com.bilgeadam.repository;

import com.bilgeadam.model.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth, Long> {
    List<Auth> findByRoles(String role);
    Optional<Auth> findOptionalByEmail(String email);
}
