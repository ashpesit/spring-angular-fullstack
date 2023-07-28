package com.wisetechglobal.exercise.persistence.respository;

import com.wisetechglobal.exercise.persistence.model.Department;

import java.util.Set;

public interface DepartmentRepository extends AbstractBaseRepository<Department,Long> {
    Set<Department> findByRequired(boolean required);

    Integer countByIdIn(Set<Long> ids);
}
