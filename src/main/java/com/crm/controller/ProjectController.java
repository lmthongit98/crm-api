package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectWithMembersDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_EDIT)
    @PostMapping
    public Object createProject(@RequestBody @Valid ProjectRequestDto projectRequestDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        ProjectResponseDto project = projectService.create(projectRequestDto);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_EDIT)
    @PostMapping("/add-members/{project-id}")
    public Object addMembers(@PathVariable("project-id") @NotNull Long projectId, @RequestBody List<Long> userIds) {
        ProjectWithMembersDto dto = projectService.addMembers(projectId, userIds);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

}
