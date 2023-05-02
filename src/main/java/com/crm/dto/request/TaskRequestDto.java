package com.crm.dto.request;

import com.crm.common.enums.Priority;
import com.crm.common.enums.TaskStatus;
import com.crm.common.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {
    @NotNull(message = "Project id must not be null.")
    private Long projectId;
    @NotBlank(message = "Task name must not be null.")
    private String name;
    private Long userId;
    private String description;
    private TaskType type;
    private Priority priority;
    private TaskStatus status;
}
