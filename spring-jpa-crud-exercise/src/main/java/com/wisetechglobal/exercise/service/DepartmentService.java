package com.wisetechglobal.exercise.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Department;

import java.util.Set;

public interface DepartmentService {
    BaseResponse<Department> createDepartment(Department department);

    BaseResponse<Department> updateDepartment(Department department);

    BaseResponse<Department> getDepartment(Long id);

    BaseResponse<Object> deleteDepartment(Long id);

    Set<Department> getRequiredDepartments();

    boolean departmentsExist(Set<Long> ids);

    BaseResponse<Set<Department>> getDepartments(Integer pageNo, Integer fetchLimit);

    BaseResponse<Long> getDepartmentCount();
}
