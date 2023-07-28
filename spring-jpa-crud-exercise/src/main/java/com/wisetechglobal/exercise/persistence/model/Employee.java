package com.wisetechglobal.exercise.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Employee extends AbstractBaseEntity{
    @NotBlank(message = "Employee first name cannot be blank")
    @Column(name = "name_first")
    private String firstName;
    @Column(name = "name_last")
    private String lastName;
    @NotBlank(message = "Email address cannot be blank")
    @Column(name = "email_address")
    private String emailAddress;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "map_employee_department", joinColumns = @JoinColumn(name = "id_employee"), inverseJoinColumns = @JoinColumn(name = "id_department"))
    private Set<Department> departments = new HashSet<>();
}
