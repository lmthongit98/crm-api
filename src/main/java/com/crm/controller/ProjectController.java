package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
