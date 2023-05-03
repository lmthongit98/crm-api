package com.crm.service;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.AbstractResponseDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectDetailResponseDto;
import com.crm.model.Project;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto create(ProjectRequestDto projectRequestDto);

    ProjectDetailResponseDto addMembers(Long projectId, List<Long> userIds);

    ProjectDetailResponseDto removeMembers(Long projectId, List<Long> userIds);

    AbstractResponseDto<ProjectDetailResponseDto> getAllProjects(String searchKey, int pageNo, int pageSize, String sortBy, String sortDir);

    ProjectDetailResponseDto findById(Long id);

    void deleteById(Long id);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto);

    Project findProjectById(Long id);
}
