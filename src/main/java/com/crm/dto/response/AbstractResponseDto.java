package com.crm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractResponseDto<T> implements Serializable {
    protected Long id;
    protected LocalDateTime createdAt;
    protected String createdBy;
    protected LocalDateTime lastModifiedAt;
    protected String lastModifiedBy;
    protected List<T> content;
    protected Integer pageNo;
    protected Integer pageSize;
    protected Long totalElements;
    protected Integer totalPages;
    protected Boolean last;

    public AbstractResponseDto(Page<?> page, List<T> content) {
        this.content = content;
        this.pageNo = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
