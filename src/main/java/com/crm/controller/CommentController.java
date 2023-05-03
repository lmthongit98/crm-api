package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.request.CommentRequestDto;
import com.crm.dto.response.CommentResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a comment")
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

    @Operation(summary = "Delete comment by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_EDIT)
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") @NotNull Long id) {
        commentService.deleteComment(id);
    }

    @Operation(summary = "Edit comment by id")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.TASK_EDIT)
    @PutMapping("/{id}")
    public Object editComment(@PathVariable("id") @NotNull Long id, @RequestBody @NotBlank String body,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        CommentResponseDto commentResponseDto = commentService.editComment(id, body);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

}
