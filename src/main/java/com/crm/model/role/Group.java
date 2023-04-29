package com.crm.model.role;

import com.crm.model.BaseEntity;
import com.crm.model.user.User;
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
@Table(name = "_group")
public class Group extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> users = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "group_role",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.getGroups().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getGroups().remove(this);
    }

}
