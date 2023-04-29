package com.crm.model.role;

import com.crm.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
    @Column(name = "url", unique = true, nullable = false, length = 100)
    private String url;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups = new HashSet<>();
}
