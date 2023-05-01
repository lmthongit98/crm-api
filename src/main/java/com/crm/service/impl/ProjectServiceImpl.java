package com.crm.service.impl;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.model.project.Project;
import com.crm.repository.ProjectRepository;
import com.crm.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProjectResponseDto create(ProjectRequestDto projectRequestDto) {
        Project project = mapToEntity(projectRequestDto);
        Project savedProject = projectRepository.save(project);
        return mapToDto(savedProject);
    }

    private Project mapToEntity(ProjectRequestDto projectRequestDto) {
        return modelMapper.map(projectRequestDto, Project.class);
    }

    private ProjectResponseDto mapToDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

}
