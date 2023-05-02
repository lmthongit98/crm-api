package com.crm.service.impl;

import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Project;
import com.crm.model.Task;
import com.crm.model.User;
import com.crm.repository.TaskRepository;
import com.crm.service.ProjectService;
import com.crm.service.TaskService;
import com.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final UserService userService;

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Project project = projectService.findProjectById(taskRequestDto.getProjectId());
        Task task = modelMapper.map(taskRequestDto, Task.class);
        task.setProject(project);
        assignTaskToUser(taskRequestDto, task);
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {
        Task task = findTaskById(id);
        task.setName(taskRequestDto.getName());
        task.setDescription(taskRequestDto.getDescription());
        task.setPriority(taskRequestDto.getPriority());
        task.setStatus(taskRequestDto.getStatus());
        task.setType(taskRequestDto.getType());
        assignTaskToUser(taskRequestDto, task);
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    private void assignTaskToUser(TaskRequestDto taskRequestDto, Task task) {
        if (!Objects.isNull(taskRequestDto.getUserId())) {
            User user = userService.findUserById(taskRequestDto.getUserId());
            task.setAssignee(user);
        }
    }

    @Override
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public TaskResponseDto findById(Long id) {
        Task task = findTaskById(id);
        return mapToDto(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found for id: " + id));
    }

    private TaskResponseDto mapToDto(Task savedTask) {
        return modelMapper.map(savedTask, TaskResponseDto.class);
    }


}
