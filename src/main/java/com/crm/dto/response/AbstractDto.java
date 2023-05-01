package com.crm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDto {
    protected Long id;
    protected LocalDateTime createdAt;
    protected String createdBy;
    protected LocalDateTime lastModifiedAt;
    protected String lastModifiedBy;
}
