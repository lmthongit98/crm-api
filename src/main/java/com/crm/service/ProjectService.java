package com.crm.service;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectResponsePagingDto;
import com.crm.dto.response.ProjectWithMembersDto;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto create(ProjectRequestDto projectRequestDto);

    ProjectWithMembersDto addMembers(Long projectId, List<Long> userIds);

    ProjectWithMembersDto removeMembers(Long projectId, List<Long> userIds);

    ProjectResponsePagingDto getAllProjects(String searchKey, int pageNo, int pageSize, String sortBy, String sortDir);

    ProjectWithMembersDto findById(Long id);

    void deleteById(Long id);

    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto);
}
