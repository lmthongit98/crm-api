package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.TaskRequestDto;
import com.crm.dto.response.TaskResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_VIEW)
    @GetMapping
    public Object filterTasksByAssignees(@RequestParam(required = false) List<Long> assigneeIds) {
        List<TaskResponseDto> tasks = taskService.filterTasksByAssignees(assigneeIds);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_EDIT)
    @PostMapping
    public Object createTask(@RequestBody @Valid TaskRequestDto taskRequestDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        TaskResponseDto taskResponseDto = taskService.createTask(taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_EDIT)
    @PutMapping("/{id}")
    public Object updateTask(@PathVariable("id") Long id, @RequestBody @Valid TaskRequestDto taskRequestDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        TaskResponseDto taskResponseDto = taskService.updateTask(id, taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_DELETION)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_VIEW)
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        TaskResponseDto taskResponseDto = taskService.findById(id);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

}
