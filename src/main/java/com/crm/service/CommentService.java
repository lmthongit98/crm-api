package com.crm.service;

import com.crm.dto.request.CommentRequestDto;
import com.crm.dto.response.CommentResponseDto;

public interface CommentService {
    CommentResponseDto addComment(CommentRequestDto commentRequestDto);
}
