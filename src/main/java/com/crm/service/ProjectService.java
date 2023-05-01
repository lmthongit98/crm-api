package com.crm.service;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectWithMembersDto;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto create(ProjectRequestDto projectRequestDto);

    ProjectWithMembersDto addMembers(Long projectId, List<Long> userIds);
}
