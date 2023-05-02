package com.crm.service;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectResponsePagingDto;
import com.crm.dto.response.ProjectDetailDto;
import com.crm.model.Project;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto create(ProjectRequestDto projectRequestDto);

    ProjectDetailDto addMembers(Long projectId, List<Long> userIds);

    ProjectDetailDto removeMembers(Long projectId, List<Long> userIds);

    ProjectResponsePagingDto getAllProjects(String searchKey, int pageNo, int pageSize, String sortBy, String sortDir);

    ProjectDetailDto findById(Long id);

    void deleteById(Long id);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto);

    Project findProjectById(Long id);
}
