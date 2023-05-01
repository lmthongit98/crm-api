package com.crm.model.role;

import com.crm.model.BaseEntity;
import com.crm.security.enums.Permission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "_role")
public class Role extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Permission name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups = new HashSet<>();
}
