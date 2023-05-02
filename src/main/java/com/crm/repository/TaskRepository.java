package com.crm.repository;

import com.crm.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee_IdIn(List<Long> assigneeIds);
}
