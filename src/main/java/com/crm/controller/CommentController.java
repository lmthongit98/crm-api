package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.CommentRequestDto;
import com.crm.dto.response.CommentResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_VIEW)
    @PostMapping
    public Object addComment(@RequestBody @Valid CommentRequestDto commentRequestDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        CommentResponseDto commentResponseDto = commentService.addComment(commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

}
