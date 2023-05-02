package com.crm.dto.response;

import com.crm.common.enums.Priority;
import com.crm.common.enums.TaskStatus;
import com.crm.common.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskResponseDto extends AbstractDto {
    private Long projectId;
    private String name;
    private String description;
    private TaskType type;
    private Priority priority;
    private TaskStatus status;
}
