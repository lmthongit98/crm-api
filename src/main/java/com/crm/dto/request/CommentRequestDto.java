package com.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotNull(message = "User id must not be null.")
    private Long userId;
    @NotNull(message = "Task id must not be null.")
    private Long taskId;
    @NotBlank(message = "Body must not be blank.")
    private String body;
}
