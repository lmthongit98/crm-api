package com.crm.controller;

import com.crm.common.constant.AppConstants;
import com.crm.common.enums.UserStatus;
import com.crm.common.util.FileUploadUtil;
import com.crm.dto.PasswordDto;
import com.crm.dto.request.UserRequestDto;
import com.crm.dto.UserToUpdateDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.dto.response.AbstractResponseDto;
import com.crm.dto.response.UserResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.UserService;
import com.crm.common.util.ErrorHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Value("${app.file-upload.root-path}")
    private String rootPath;


    @Operation(summary = "Create new user with avatar")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PostMapping(value = "/with-avatar")
    public Object createNewUser(@RequestParam String username, @RequestParam String password,
                                @RequestParam String displayName, @RequestParam String email,
                                @RequestParam MultipartFile file) throws IOException {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username(username)
                .password(password)
                .displayName(displayName)
                .email(email)
                .status(UserStatus.ACTIVE)
                .build();
        UserResponseDto savedUser;
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            userRequestDto.setAvatar(fileName);
            savedUser = userService.save(userRequestDto);
            Path uploadDir = Paths.get(rootPath).resolve(savedUser.getId().toString());
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        } else {
            savedUser = userService.save(userRequestDto);
        }
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user avatar")
    @GetMapping("/file/{id}/{fileName:.+}")
    public Object getUserAvatar(@PathVariable Long id, @PathVariable String fileName) {
        Resource resource = userService.loadUserAvatarImg(id, fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @Operation(summary = "Create new user")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PostMapping
    public Object createNewUser(@RequestBody @Valid UserRequestDto userRequestDto, @RequestParam MultipartFile file, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        UserResponseDto savedUser = userService.save(userRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @Operation(summary = "Update user by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PutMapping("/{id}")
    public Object updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserToUpdateDto userToUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        UserResponseDto updatedUser = userService.update(id, userToUpdateDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @Operation(summary = "Change password")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PutMapping("/change-password/{user-id}")
    public Object changePassword(@PathVariable("user-id") Long userId, @RequestBody @Valid PasswordDto passwordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(userId, passwordDto);
        return new ResponseEntity<>("Changed password successfully!", HttpStatus.OK);
    }

    @Operation(summary = "Search users by username or display name")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_VIEW)
    @GetMapping
    public Object getAllUsers(@RequestParam(value = "searchKey", defaultValue = AppConstants.EMPTY, required = false) String searchKey,
                              @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        AbstractResponseDto<UserResponseDto> responseDto = userService.searchUsers(searchKey, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Find user's roles by user id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_VIEW)
    @GetMapping ("/{user-id}/roles")
    public Object findRoleByUserId(@PathVariable("user-id") @NotNull Long userId) {
        UserWithRolesDto userWithRolesDto = userService.findUserWithRolesById(userId);
        return new ResponseEntity<>(userWithRolesDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete users by ids")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_DELETION)
    @DeleteMapping
    public void deleteUser(@RequestParam @NotEmpty List<Long> ids) {
        userService.delete(ids);
    }

}
