package com.crm.controller;

import com.crm.common.constant.AppConstants;
import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.AbstractResponseDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectDetailResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Search projects by name or description")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_VIEW)
    @GetMapping
    public Object getAllProjects(
            @RequestParam(value = "searchKey", defaultValue = AppConstants.EMPTY, required = false) String searchKey,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        AbstractResponseDto<ProjectDetailResponseDto> responseDto = projectService.getAllProjects(searchKey, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Find project by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_VIEW)
    @GetMapping("/{id}")
    public Object findProjectById(@PathVariable("id") @NotNull Long id){
        ProjectDetailResponseDto dto = projectService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Create a project")
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

    @Operation(summary = "Delete a project by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_DELETION)
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") Long id) {
        projectService.deleteById(id);
    }

    @Operation(summary = "Update a project by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_EDIT)
    @PutMapping("/{id}")
    public Object updateProject(@PathVariable("id") Long projectId, @RequestBody @Valid ProjectRequestDto projectRequestDto,
                                BindingResult bindingResult) {
        ProjectResponseDto dto = projectService.updateProject(projectId, projectRequestDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @Operation(summary = "Add members into project")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_EDIT)
    @PostMapping("/add-members/{project-id}")
    public Object addMembers(@PathVariable("project-id") @NotNull Long projectId, @RequestBody List<Long> userIds) {
        ProjectDetailResponseDto dto = projectService.addMembers(projectId, userIds);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Remove members from project")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.PROJECT_EDIT)
    @PostMapping("/remove-members/{project-id}")
    public Object removeMembers(@PathVariable("project-id") @NotNull Long projectId, @RequestBody List<Long> userIds) {
        ProjectDetailResponseDto dto = projectService.removeMembers(projectId, userIds);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
