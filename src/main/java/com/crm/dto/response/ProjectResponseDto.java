package com.crm.dto.response;

import com.crm.common.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDto extends AbstractResponseDto<ProjectResponseDto> {
    private String name;
    private String description;
    private ProjectType type;
}
