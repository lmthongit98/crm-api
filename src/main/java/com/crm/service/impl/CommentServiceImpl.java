package com.crm.service.impl;

import com.crm.dto.request.CommentRequestDto;
import com.crm.dto.response.CommentResponseDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Comment;
import com.crm.model.Task;
import com.crm.model.User;
import com.crm.repository.CommentRepository;
import com.crm.service.CommentService;
import com.crm.service.TaskService;
import com.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        Task task = taskService.findTaskById(commentRequestDto.getTaskId());
        User user = userService.findUserById(commentRequestDto.getUserId());
        Comment comment = Comment.builder()
                .body(commentRequestDto.getBody())
                .task(task)
                .user(user)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return mapToDto(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = findCommentById(id);
        commentRepository.delete(comment);
    }

    @Override
    public CommentResponseDto editComment(Long id, String body) {
        Comment comment = findCommentById(id);
        comment.setBody(body);
        Comment savedComment = commentRepository.save(comment);
        return mapToDto(savedComment);
    }

    private CommentResponseDto mapToDto(Comment savedComment) {
        return modelMapper.map(savedComment, CommentResponseDto.class);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found for id: " + id));
    }

}
