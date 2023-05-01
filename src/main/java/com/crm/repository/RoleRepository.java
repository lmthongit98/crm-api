package com.crm.repository;

import com.crm.model.role.Role;
import com.crm.security.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Permission name);
}
