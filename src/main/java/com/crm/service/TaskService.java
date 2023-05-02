package com.crm.service;

import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);

    void deleteTask(Long id);

    TaskResponseDto findById(Long id);

    List<TaskResponseDto> filterTasksByAssignees(List<Long> assigneeIds);
}
