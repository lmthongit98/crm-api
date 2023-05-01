package com.crm.dto.response;

import com.crm.common.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectWithMembersDto extends AbstractDto {
    private String name;
    private String description;
    private ProjectType type;
    private List<UserResponseDto> members;
}
