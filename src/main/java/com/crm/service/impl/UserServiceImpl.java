package com.crm.service.impl;

import com.crm.common.enums.UserStatus;
import com.crm.controller.UserController;
import com.crm.dto.PasswordDto;
import com.crm.dto.RoleDto;
import com.crm.dto.UserToUpdateDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.dto.request.UserRequestDto;
import com.crm.dto.response.AbstractResponseDto;
import com.crm.dto.response.UserResponseDto;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.User;
import com.crm.repository.UserRepository;
import com.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.file-upload.root-path}")
    private String rootPath;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = mapToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public UserWithRolesDto findUserWithRolesById(Long userId) {
        User user = findUserById(userId);
        UserWithRolesDto userWithRolesDto = UserWithRolesDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .build();
        if (!Objects.isNull(user.getGroup()) && !CollectionUtils.isEmpty(user.getGroup().getRoles())) {
            List<RoleDto> roles = user.getGroup().getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).toList();
            userWithRolesDto.setRoles(roles);
        }
        return userWithRolesDto;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("User not found for username: " + username));
    }

    @Override
    public UserResponseDto update(Long id, UserToUpdateDto userToUpdateDto) {
        User user = findUserById(id);
        user.setDisplayName(userToUpdateDto.getUsername());
        if (!user.getUsername().equals(userToUpdateDto.getUsername())) {
            if (userRepository.findByUsername(userToUpdateDto.getUsername()).isPresent()) {
                throw new BadRequestException("Username is already used.");
            }
            user.setUsername(userToUpdateDto.getUsername());
        }
        if (!user.getEmail().equals(userToUpdateDto.getEmail())) {
            if (userRepository.findByEmail(userToUpdateDto.getEmail()).isPresent()) {
                throw new BadRequestException("Email is already used.");
            }
            user.setEmail(userToUpdateDto.getEmail());
        }
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void changePassword(Long userId, PasswordDto passwordDto) {
        User user = findUserById(userId);
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is not correct!");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password is not correct!");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            User user = findUserById(id);
            user.setStatus(UserStatus.DELETED);
            userRepository.save(user);
        }
    }

    @Override
    public AbstractResponseDto<UserResponseDto> searchUsers(String searchKey, int pageNo, int pageSize, String sortBy, String sortDir) {
        logger.info("Starting search user with keyword: {}", searchKey);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.searchUsers(searchKey, pageable);
        List<User> listOfUsers = users.getContent();
        List<UserResponseDto> content = listOfUsers.stream().map(this::mapToDto).toList();
        return new AbstractResponseDto<>(users, content);
    }

    @Override
    public Resource loadUserAvatarImg(Long id, String fileName) {
        try {
            Path root = Paths.get(rootPath);
            Path file = root.resolve(id.toString()).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (Exception e) {
            logger.error("Fail to load file", e);
        }
        return null;
    }

    private UserResponseDto mapToDto(User user) {
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        if(StringUtils.isNotEmpty(user.getAvatar())) {
            final String GET_USER_AVATAR = "getUserAvatar";
            final String avatarUrlPath = MvcUriComponentsBuilder.fromMethodName(UserController.class, GET_USER_AVATAR, user.getId(), user.getAvatar()).build().toUriString();
            userResponseDto.setAvatar(avatarUrlPath);
        }
        return userResponseDto;
    }

    private User mapToEntity(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto, User.class);
    }

}
