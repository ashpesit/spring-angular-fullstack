package com.wisetechglobal.exercise.persistence.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Department extends AbstractBaseEntity {
    @NotBlank(message = "Department name should not be blank")
    @EqualsAndHashCode.Exclude
    private String name;
    @NotNull(message = "Read only flag should not be null")
    @Column(name = "read_only")
    @EqualsAndHashCode.Exclude
    private Boolean readOnly;
    @NotNull(message = "Required flag should not be null")
    @EqualsAndHashCode.Exclude
    private Boolean required;
    @JsonIgnore
    @ManyToMany(mappedBy = "departments",fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Employee> employees = new HashSet<>();
}
