package com.crm.service.impl;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectWithMembersDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.project.Project;
import com.crm.model.user.User;
import com.crm.repository.ProjectRepository;
import com.crm.repository.UserRepository;
import com.crm.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public ProjectResponseDto create(ProjectRequestDto projectRequestDto) {
        Project project = mapToEntity(projectRequestDto);
        Project savedProject = projectRepository.save(project);
        return mapToDto(savedProject);
    }

    @Override
    public ProjectWithMembersDto addMembers(Long projectId, List<Long> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userRepository.findAllById(userIds);
        project.addMember(new HashSet<>(users));
        Project savedProject = projectRepository.save(project);
        return modelMapper.map(savedProject, ProjectWithMembersDto.class);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + id));
    }

    private Project mapToEntity(ProjectRequestDto projectRequestDto) {
        return modelMapper.map(projectRequestDto, Project.class);
    }

    private ProjectResponseDto mapToDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

}
