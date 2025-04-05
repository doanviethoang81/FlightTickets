package com.example.banvemaybay.repositorys;

import com.example.banvemaybay.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepostiory extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String roleUser);
}
