package com.crm.repository;

import com.crm.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE " +
            "p.name LIKE CONCAT('%', :searchKey, '%') " +
            "OR p.description LIKE CONCAT('%', :searchKey, '%')")
    Page<Project> searchProjects(String searchKey, Pageable pageable);
}
