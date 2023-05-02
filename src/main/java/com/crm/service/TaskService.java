package com.crm.service;

import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);
}
