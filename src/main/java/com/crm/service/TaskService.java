package com.crm.service;

import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;
import com.crm.model.Task;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);

    void deleteTask(Long id);

    TaskResponseDto findById(Long id);

    Task findTaskById(Long id);

    List<TaskResponseDto> filterTasksByAssignees(List<Long> assigneeIds);
}
