package com.crm.service;

import com.crm.dto.request.CommentRequestDto;
import com.crm.dto.response.CommentResponseDto;

public interface CommentService {
    CommentResponseDto addComment(CommentRequestDto commentRequestDto);

    void deleteComment(Long id);

    CommentResponseDto editComment(Long id, String body);
}
