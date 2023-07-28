package com.wisetechglobal.exercise.service;

import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.persistence.model.Employee;

import java.util.Set;

public interface EmployeeService {
    BaseResponse<Employee> createEmployee(Employee employee);

    BaseResponse<Employee> getEmployee(Long id);

    BaseResponse<Employee> updateEmployee(Employee employee);

    BaseResponse<Object> deleteEmployee(Long id);

    BaseResponse<Set<Employee>> getAllEmployees(Integer pageNo, Integer fetchLimit);

    BaseResponse<Long> getEmployeeCount();
}
