package com.crm.dto.request;

import com.crm.common.enums.ProjectType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequestDto {
    private String name;
    private String description;
    private ProjectType type;
}
