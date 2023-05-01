package com.crm.service;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto create(ProjectRequestDto projectRequestDto);
}
