package com.crm.service.impl;

import com.crm.dto.request.ProjectRequestDto;
import com.crm.dto.response.AbstractResponseDto;
import com.crm.dto.response.ProjectResponseDto;
import com.crm.dto.response.ProjectDetailResponseDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Project;
import com.crm.model.User;
import com.crm.repository.ProjectRepository;
import com.crm.repository.UserRepository;
import com.crm.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ProjectDetailResponseDto addMembers(Long projectId, List<Long> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userRepository.findAllById(userIds);
        project.addMember(new HashSet<>(users));
        Project savedProject = projectRepository.save(project);
        return mapToProjectWithMembersDto(savedProject);
    }

    @Override
    public ProjectDetailResponseDto removeMembers(Long projectId, List<Long> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userRepository.findAllById(userIds);
        project.removeMember(new HashSet<>(users));
        Project savedProject = projectRepository.save(project);
        return mapToProjectWithMembersDto(savedProject);
    }

    @Override
    public AbstractResponseDto<ProjectDetailResponseDto> getAllProjects(String searchKey, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Project> projects = projectRepository.searchProjects(searchKey, pageable);
        List<Project> listOfProjects = projects.getContent();
        List<ProjectDetailResponseDto> content = listOfProjects.stream().map(this::mapToProjectWithMembersDto).toList();
        return new AbstractResponseDto<>(projects, content);
    }

    @Override
    public ProjectDetailResponseDto findById(Long id) {
        Project project = findProjectById(id);
        return mapToProjectWithMembersDto(project);
    }

    @Override
    public void deleteById(Long id) {
        Project project = findProjectById(id);
        projectRepository.delete(project);
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto) {
        Project project = findProjectById(projectId);
        project.setName(projectRequestDto.getName());
        project.setDescription(projectRequestDto.getDescription());
        project.setType(projectRequestDto.getType());
        Project savedProject = projectRepository.save(project);
        return mapToDto(savedProject);
    }

    @Override
    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + id));
    }

    private ProjectDetailResponseDto mapToProjectWithMembersDto(Project savedProject) {
        return modelMapper.map(savedProject, ProjectDetailResponseDto.class);
    }

    private Project mapToEntity(ProjectRequestDto projectRequestDto) {
        return modelMapper.map(projectRequestDto, Project.class);
    }

    private ProjectResponseDto mapToDto(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }

}
