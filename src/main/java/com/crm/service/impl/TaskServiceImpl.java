package com.crm.service.impl;

import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;
import com.crm.model.Project;
import com.crm.model.Task;
import com.crm.repository.TaskRepository;
import com.crm.service.ProjectService;
import com.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    private final ProjectService projectService;

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Project project = projectService.findProjectById(taskRequestDto.getProjectId());
        Task task = modelMapper.map(taskRequestDto, Task.class);
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponseDto.class);
    }

}
