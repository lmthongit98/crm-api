package com.crm.repository;

import com.crm.model.role.Role;
import com.crm.security.enums.SecurityAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(SecurityAuthority name);
}
